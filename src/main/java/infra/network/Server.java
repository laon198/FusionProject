package infra.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static ServerSocket serverSocket;
    private static ServerThread clients[];
    private static int clientCount;

    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
        clients = new ServerThread[50];
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
            clients[clientCount] = new ServerThread(socket);
            clients[clientCount].start();
            System.out.println("client Port : " + clients[clientCount].getClientID()    );
            clientCount++;
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
    public synchronized static void removeThread(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ServerThread st = clients[pos];
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
            System.out.print("clientCount : " + clientCount);
            st.interrupt();
        }
    }

}
