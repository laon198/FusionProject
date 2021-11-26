package controller;

import infra.dto.AccountDTO;
import infra.network.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginController implements Controller {
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private int clientID;

    public LoginController(Socket socket, InputStream is,
                           OutputStream os, int clientID){
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.clientID = clientID;
    }

    public int handler(Protocol recvPt){
        try{
            switch (recvPt.getCode()) {
                case Protocol.T1_CODE_LOGIN:   // 로그인
                    loginReq(recvPt);
                    return 1;
                case Protocol.T1_CODE_LOGOUT:  // 로그아웃
                    logoutReq();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void loginReq(Protocol recvPt) throws Exception{
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        AccountDTO accDTO = (AccountDTO) recvPt.getObject();

        // < DB >
        // 로그인 성공

        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);

//            // 로그인 실패
//            sendPt.setCode(Protocol.T2_CODE_FAIL);
//            send(sendPt);

    }

    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }


}

