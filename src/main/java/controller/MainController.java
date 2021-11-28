package controller;

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
    private DefinedController myController;

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
                handler(pt.read(is));  // pt.read(is)는 pt 반환
            } catch (Exception e) {
                System.err.println(e);
                try {
                    stopThread();
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        }
        System.out.println("thread 종료");
    }

    public int getClientID() {
        return clientID;
    }

    public void handler(Protocol pt) throws Exception {
        // protocol type이 request가 아닌 경우 실패 메시지 전송
        if (pt.getType() != Protocol.TYPE_REQUEST) {
            Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE, Protocol.T2_CODE_FAIL);
            sendPt.send(os);
            return;
        }

        // 종료 요청 받은 경우
        if (pt.getType() == Protocol.TYPE_REQUEST &&
                pt.getCode() == Protocol.T1_CODE_EXIT) {
            throw new Exception(clientID + "와 통신 종료 (정상종료)");
        }

        switch(userType){
            case USER_UNDEFINED: // 사용자 로그인 전
                UndefinedController undefinedController = new UndefinedController(is, os, accRepo);
//                UndefinedController undefinedController = new UndefinedController(
//                        socket, is, os, clientID, accRepo
//                );
                userType = undefinedController.handler(pt);
                setMyController();
                break;
            case STUD_TYPE:
            case PROF_TYPE:
            case ADMIN_TYPE:
                userType = myController.handler(pt);
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
            default:
        }
    }

    private void stopThread() throws IOException {
        Server.removeThread(clientID);
        socket.close();
        running = false;
    }
}
