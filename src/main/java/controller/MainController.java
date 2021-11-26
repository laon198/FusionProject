package controller;

import application.AccountAppService;
import controller.AdminController;
import controller.Controller;
import controller.LoginController;
import controller.ProfessorController;
import controller.StudentController;
import infra.dto.AccountDTO;
import infra.network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class MainController extends Thread {
    // USER 구분
    public static final int USER_UNDEFINED = 0;
    public static final int USER_DEFINED = 1;

    public static final int STUD_TYPE = 0;
    public static final int PROF_TYPE = 1;
    public static final int ADMIN_TYPE = 2;
    private int userType;

    private int clientID;
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private Controller myController;

    // 스레드 생성자
    public MainController(Socket socket) throws IOException {
        userType = USER_UNDEFINED;
        clientID = socket.getPort();
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    @Override
    public void run() {
        boolean programRun = true;
        while (programRun) {
            try {
                Protocol pt = new Protocol();
                handler(pt.read(is));
            } catch (IOException e) {
                System.out.println("ServerTh.run() -> IOException");
                this.interrupt();
            } catch (Exception e) {
                System.out.println("Exception");
            }
        }
    }

    public int getClientID() {
        return clientID;
    }

    public void handler(Protocol pt){
        switch(userType){
            case USER_UNDEFINED:
                Controller loginController = new LoginController(
                        socket, is, os, clientID
                );
                userType = loginController.handler(pt);
                setMyController();
                userType = USER_DEFINED;
                break;

            case USER_DEFINED:
                myController.handler(pt);
                break;
        }
    }

    private void setMyController() {
        switch (userType){
            case STUD_TYPE:
                myController = new StudentController();
                break;
            case PROF_TYPE:
                myController = new ProfessorController();
                break;
            case ADMIN_TYPE:
                myController = new AdminController();
                break;
        }
    }
}
