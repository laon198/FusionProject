package infra.network;

import controller.MainController;
import domain.repository.*;
import org.apache.log4j.chainsaw.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;

    public Server(
            AccountRepository accRepo, AdminRepository adminRepo, CourseRepository courseRepo,
            LectureRepository lectureRepo, ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo
    ){
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
        this.courseRepo = courseRepo;
        this.lectureRepo = lectureRepo;
        this.profRepo = profRepo;
        this.regRepo = regRepo;
        this.regPeriodRepo = regPeriodRepo;
        this.stdRepo = stdRepo;
        try{
            serverSocket = new ServerSocket(3000);
            clients = new MainController[50];
            clientCount = 0;
        }catch(Exception e){
            e.getStackTrace();
        }
    }

    private static ServerSocket serverSocket;
    private static MainController clients[];
    private static int clientCount;


    // 구동
    public void run() {
        System.out.println("Server running ...");
        while (serverSocket != null) {
            try {
                // 소켓연결
                Socket socket = serverSocket.accept();
                System.out.println("Success socket connection");
                // 스레드 생성
                addThread(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addThread(Socket socket) throws IOException {
        if (clientCount < clients.length) {
            MainController thread = new MainController(
                    accRepo, adminRepo, courseRepo,
                    lectureRepo, profRepo, regRepo,
                    regPeriodRepo, stdRepo, socket
            );
            clients[clientCount++] = thread;
            System.out.println("Create thread > clientCount = " + clientCount);
            System.out.println("client Port : " + clients[clientCount].getClientID());
            clients[clientCount++].start();
            System.out.println("clientCount : " + clientCount);
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

    public static int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getClientID() == ID)
                return i;
        return -1;
    }

    // 쓰레드 지우기
    public synchronized static void removeThread(int ID) throws IOException {
        int pos = findClient(ID);
        if (pos >= 0) {
            MainController thread = clients[pos];
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
            System.out.print("clientCount : " + clientCount);
            thread.socketClose();
        }
    }

}
