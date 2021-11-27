package controller;

import application.AccountAppService;
import controller.AdminController;
import controller.Controller;
import controller.LoginController;
import controller.ProfessorController;
import controller.StudentController;
import domain.repository.*;
import infra.database.repository.*;
import infra.dto.AccountDTO;
import infra.network.Protocol;
import infra.network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private AccountRepository accRepo;
    private AdminRepository adminRepo;
    private CourseRepository courseRepo;
    private LectureRepository lectureRepo;
    private ProfessorRepository profRepo;
    private RegisteringRepository regRepo;
    private RegPeriodRepository regPeriodRepo;
    private StudentRepository stdRepo;

    // 스레드 생성자

    public MainController(
            AccountRepository accRepo, AdminRepository adminRepo, CourseRepository courseRepo,
            LectureRepository lectureRepo, ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo, Socket socket
    ){
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
        this.courseRepo = courseRepo;
        this.lectureRepo = lectureRepo;
        this.profRepo = profRepo;
        this.regRepo = regRepo;
        this.regPeriodRepo = regPeriodRepo;
        this.stdRepo = stdRepo;
        this.socket = socket;
        clientID = socket.getPort();
        this.socket = socket;
        try{
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    private boolean running;
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Protocol pt = new Protocol();
                handler(pt.read(is));
            } catch (IOException e) {
                exit();
            } catch (Exception e) {
                System.out.println("Exception");
            }
        }
        System.out.println("thread 종료");
    }

    public int getClientID() {
        return clientID;
    }

    public void handler(Protocol pt) throws Exception {
        if (pt.getType() == Protocol.TYPE_REQUEST && pt.getCode() == Protocol.T1_CODE_EXIT)
        {
            exit();
            return;
        }

        switch(userType){
            case USER_UNDEFINED:
                Controller loginController = new LoginController(
                        socket, is, os, clientID, accRepo
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
                myController = new StudentController(is, os);
                break;
            case PROF_TYPE:
                myController = new ProfessorController(is, os);
                break;
            case ADMIN_TYPE:
                myController = new AdminController(is, os);
                break;
        }
    }


    public void socketClose() throws IOException
    {
        socket.close();
        is.close();
        os.close();
    }

    private void exit() throws IOException {
        Server.removeThread(clientID);
        running = false;
    }
}
