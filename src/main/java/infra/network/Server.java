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
                System.err.println(e);
            }
        }
    }

    public synchronized void addThread(Socket socket) throws IOException {
        if (clientCount < clients.length) {
            MainController thread = new MainController(socket);
            clients[clientCount++] = thread;
            System.out.println("Create thread : clientCount = " + clientCount);
            System.out.println("client Port : " + thread.getClientID());
            thread.start();
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
            socket.close();
        }
    }

    // clients 배열에서 해당 ID(port)를 가진 client pos 리턴
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
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++)
                    clients[i - 1] = clients[i];
            clientCount--;
        }
    }
}
