package controller;

import application.AccountAppService;
import domain.repository.AccountRepository;
import infra.dto.AccountDTO;
import infra.network.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginController implements Controller {
    public static final int FAIL = -1;
    public static final int STUD_TYPE = 0;
    public static final int PROF_TYPE = 1;
    public static final int ADMIN_TYPE = 2;

    private AccountRepository accRepo;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private int clientID;

    public LoginController(Socket socket, InputStream is, OutputStream os,
                           int clientID, AccountRepository accRepo){
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.clientID = clientID;
        this.accRepo = accRepo;
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
                case Protocol.T1_CODE_EXIT:    // 종료
                    exitReq();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int loginReq(Protocol recvPt) throws Exception{
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        AccountDTO accDTO = (AccountDTO) recvPt.getObject();

        try{
            AccountAppService accService = new AccountAppService(accRepo);
            AccountDTO resAccDTO= accService.login(accDTO);

            // < DB >
            // 로그인 성공
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObject(resAccDTO);
            sendPt.send(os);

            if(resAccDTO.getPosition().equals("STUD")){
                return STUD_TYPE;
            }else if(resAccDTO.getPosition().equals("PROF")){
                return PROF_TYPE;
            }else{
                return ADMIN_TYPE;
            }
        }catch(IllegalArgumentException e){
//            // 로그인 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }

        return FAIL;
    }

    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }

    private void exitReq() throws IOException {
        socket.close();
        Server.removeThread(clientID);
    }
}

