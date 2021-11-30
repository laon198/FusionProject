package controller;

import application.RegisterAppService;
import domain.repository.*;
import infra.network.Protocol;
import infra.network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainController extends Thread {
    // USER 구분
    public static final int USER_UNDEFINED = 0;
    public static final int STUD_TYPE = 1;
    public static final int PROF_TYPE = 2;
    public static final int ADMIN_TYPE = 3;
    private int userType;

    private int clientID;
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private boolean running;
    private DefinedController myController;

    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;
    private final PlannerPeriodRepository plannerPeriodRepo;
    private final RegisterAppService regService;

    // 스레드 생성자
    public MainController(
            AccountRepository accRepo, AdminRepository adminRepo, CourseRepository courseRepo,
            LectureRepository lectureRepo, ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo,
            PlannerPeriodRepository plannerPeriodRepo, RegisterAppService regService, Socket socket
    ){
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
        this.courseRepo = courseRepo;
        this.lectureRepo = lectureRepo;
        this.profRepo = profRepo;
        this.regRepo = regRepo;
        this.regPeriodRepo = regPeriodRepo;
        this.stdRepo = stdRepo;
        this.plannerPeriodRepo = plannerPeriodRepo;
        this.regService = regService;
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

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                System.out.println("main controller entry");
                Protocol pt = new Protocol();
                handler(pt.read(is));
            } catch (Exception e) {
                e.printStackTrace();
//                System.err.println(e.getStackTrace());
                exit();
            }
        }
        System.out.println("thread 종료");
    }

    public int getClientID() {
        return clientID;
    }

    public void handler(Protocol pt) throws Exception {
        System.out.println("handler entry");
        if (pt.getType() == Protocol.TYPE_REQUEST &&
                pt.getCode() == Protocol.T1_CODE_EXIT){
            exit();
            return;
        }

        switch(userType){
            case USER_UNDEFINED:
                UndefinedController undefinedController = new UndefinedController(
                        socket, is, os, clientID, accRepo, adminRepo
                );
                userType = undefinedController.handler(pt);
                setMyController();
                break;

            case STUD_TYPE:
            case PROF_TYPE:
            case ADMIN_TYPE:
                userType = myController.handler(pt);
                setMyController();
                break;
        }
    }

    private void setMyController() {
        switch (userType){
            case STUD_TYPE:
                if(myController==null){
                    myController = new StudentController(
                            accRepo, adminRepo, courseRepo,
                            lectureRepo, profRepo, regRepo,
                            regPeriodRepo, stdRepo, plannerPeriodRepo,
                            regService, is, os
                    );
                }
                break;
            case PROF_TYPE:
                if(myController==null){
                    myController = new ProfessorController(
                            accRepo, adminRepo, courseRepo,
                            lectureRepo, profRepo, regRepo,
                            regPeriodRepo, stdRepo, plannerPeriodRepo,
                            is, os
                    );
                }
                break;
            case ADMIN_TYPE:
                if(myController==null){
                    myController = new AdminController(
                            accRepo, adminRepo, courseRepo,
                            lectureRepo, profRepo, regRepo,
                            regPeriodRepo, stdRepo, plannerPeriodRepo,
                            regService, is, os
                    );
                }
                break;
            case USER_UNDEFINED:
                myController = null;
                break;
        }
    }

    // 소켓 종료 및 스레드 종료
    private void exit() {
        Server.removeThread(clientID);
        socketClose();
        running = false;
    }

    public void socketClose() {
        try {
            socket.close();
            is.close();
            os.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
