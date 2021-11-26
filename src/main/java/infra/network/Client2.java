package infra.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client2 {

    public static void main(String[] args) throws IOException{

        Client2 client = new Client2();

        Protocol sendPt = new Protocol(Protocol.TYPE_REQUEST);
        sendPt.setCode(Protocol.T1_CODE_LOGIN);
        send(sendPt);

        while (true)
        {
            protocol = read();
            if (protocol != null)
            {
                if (protocol.getType() == Protocol.TYPE_RESPONSE)
                {
                    if (protocol.getCode() == Protocol.T2_CODE_SUCCESS)
                        System.out.println(">>>> 로그인 성공");
                    else if (protocol.getCode() == Protocol.T2_CODE_FAIL)
                        System.out.println(">>>> 로그인 실패");
                    else
                        System.out.println(">>> 얜 뭐야");
                }
            }
        }
    }

    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;
    private static Protocol protocol; // 받은 프로토콜

    public Client2() throws IOException {
        Socket socket = new Socket("localhost", 3000);
        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    // 프로토콜 송신
    private static void send(Protocol pt) throws IOException {
        os.write(pt.getPacket());
        //os.flush();
        System.out.println("서버에게 전송");
    }

    // 프로토콜 수신
    private static Protocol read() throws IOException {
        byte[] header = new byte[Protocol.LEN_HEADER];
        Protocol pt = new Protocol();
        int totalReceived = 0;
        int readSize;

        is.read(header, 0, Protocol.LEN_HEADER);
        pt.setHeader(header);

        byte[] buf = new byte[pt.getBodyLength()];
        while (totalReceived < pt.getBodyLength()) {
            readSize = is.read(buf, totalReceived, pt.getBodyLength() - totalReceived);
            totalReceived += readSize;
        }
        pt.setBody(buf);
        return pt;
    }
}


