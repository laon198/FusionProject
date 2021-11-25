package infra.network;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
    // USER 구분
    public static final int USER_UNDEFINED = 0;
    public static final int USER_STUDENT = 1;
    public static final int USER_ADMIN = 2;
    public static final int USER_PROFESSOR = 3;
    private int user;

    private int clientID;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private Protocol protocol; // 받은 프로토콜

    private StudController stud;
    private AdminController admin;
    private ProfController prof;

    // 스레드 생성자
    public ServerThread(Socket socket) throws IOException {
        user = USER_UNDEFINED;
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
                protocol = read();
                handler(protocol);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public int getClientID() {
        return clientID;
    }

    // 프로토콜 송신
    private void send(Protocol pt) throws IOException {
        os.write(pt.getPacket());
        //os.flush();
        System.out.println("클라이언트에게 전송");
    }

    // 프토토콜 수신
    private Protocol read() throws IOException {
        byte[] header = new byte[Protocol.LEN_HEADER];
        Protocol pt = new Protocol();
        int totalReceived = 0;
        int readSize;

        is.read(header, 0, Protocol.LEN_HEADER);
        pt.setPacketHeader(header);

        byte[] buf = new byte[pt.getBodyLength()];
        while (totalReceived < pt.getBodyLength()) {
            readSize = is.read(buf, totalReceived, pt.getBodyLength() - totalReceived);
            totalReceived += readSize;
        }
        pt.setPacketBody(buf);
        return pt;
    }

    private void handler(Protocol recvPt) throws IOException {
        if (recvPt.getType() != 1) {
            Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            send(sendPt);
        }

        switch (recvPt.getCode()) {
            case Protocol.T1_CODE_LOGIN:   // 로그인
                loginReq(recvPt);
                break;
            case Protocol.T1_CODE_LOGOUT:  // 로그아웃
                logoutReq();
                break;
            case Protocol.T1_CODE_EXIT:    // 종료
                exitReq();
                break;
            default : // 사용자 종류별 전용 Controller
                if (user == USER_STUDENT) {
                    stud.handler(recvPt);
                    send(stud.getSendPt());
                } else if (user == USER_ADMIN) {
                    admin.handler(recvPt);
                    send(admin.getSendPt());
                } else if (user == USER_PROFESSOR) {
                    prof.handler(recvPt);
                    send(prof.getSendPt());
                } else {
                    Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    send(sendPt);
                }
        }
    }

    private void loginReq(Protocol recvPt) throws IOException
    {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        Object o = recvPt.getBody();
        try {
            // < DB >
            // 로그인 성공

            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            send(sendPt);

            String str = "학생"; // test용
            if (str.equals("학생")) {
                user = USER_STUDENT;
                stud = new StudController();
            } else if (str.equals("관리자")) {
                user = USER_ADMIN;
                admin = new AdminController();
            } else if (str.equals("교수")) {
                user = USER_PROFESSOR;
                prof = new ProfController();
            }
        } catch (IllegalArgumentException e) {
            // 로그인 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            send(sendPt);
        }

    }

    private void logoutReq() throws IOException
    {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        send(sendPt);
        user = USER_UNDEFINED;
        if (stud != null)
            stud = null;
        if (admin != null)
            admin = null;
        if (prof != null)
            prof = null;
    }

    private void exitReq() throws IOException {
        socket.close();
        Server.removeThread(clientID);
    }
}
