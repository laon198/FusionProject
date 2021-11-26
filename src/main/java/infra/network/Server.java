package infra.network;

import controller.MainController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket serverSocket;
    private static MainController clients[];
    private static int clientCount;

    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
        clients = new MainController[50];
        clientCount = 0;
    }

    // 구동
    public void run() throws Exception {
        System.out.println("Server running ...");
        while (serverSocket != null) {
            // 소켓연결
            Socket socket = serverSocket.accept();
            System.out.println("Success socket connection");
            // 스레드 생성
            addThread(socket);
            System.out.println("Create thread");
        }
    }

    public synchronized void addThread(Socket socket) throws Exception {
        if (clientCount < clients.length) {
            clients[clientCount] = new MainController(socket);
            clients[clientCount].start();
            System.out.println("client Port : " + clients[clientCount].getClientID()    );
            clientCount++;
            System.out.print("clientCount : " + clientCount);
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
            MainController st = clients[pos];
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
            System.out.print("clientCount : " + clientCount);
            st.interrupt();
        }
    }

}
