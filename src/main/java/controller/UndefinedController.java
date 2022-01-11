package controller;

import application.AccountAppService;
import application.AdminAppService;
import domain.repository.AccountRepository;
import domain.repository.AdminRepository;
import dto.AccountDTO;
import dto.AdminDTO;
import dto.MessageDTO;
import infra.network.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//로그인전에 요청을 수행하는 객체
public class UndefinedController {
    public static final int USER_UNDEFINED = 0;
    public static final int STUD_TYPE = 1;
    public static final int PROF_TYPE = 2;
    public static final int ADMIN_TYPE = 3;

    private AccountRepository accRepo;
    private AdminRepository adminRepo;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private int clientID;

    public UndefinedController(Socket socket, InputStream is, OutputStream os,
                               int clientID, AccountRepository accRepo, AdminRepository adminRepo){
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.clientID = clientID;
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
    }

    public int handler(Protocol recvPt){
        try{
            switch (recvPt.getCode()) {
                case Protocol.T1_CODE_LOGIN:   // 로그인 요청
                    return loginReq(recvPt);
                case Protocol.T1_CODE_CREATE: // 관리자 생성 요청
                    createAdmin(recvPt);
                    return USER_UNDEFINED;
                case Protocol.T1_CODE_LOGOUT:  // 로그아웃 요청
                    logoutReq();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return USER_UNDEFINED;
    }

    // 로그인
    private int loginReq(Protocol recvPt) throws Exception{
        System.out.println("login entry");
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        AccountDTO accDTO = (AccountDTO) recvPt.getObject();
        // 클라이언트로부터 받은 accountDTO로 로그인
        try{
            AccountAppService accService = new AccountAppService(accRepo);
            AccountDTO resAccDTO= accService.login(accDTO);
            // 로그인 성공 - 성공 메시지 전송
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObject(resAccDTO);
            sendPt.send(os);

            // return userType
            if(resAccDTO.getPosition().equals("STUD")){
                return STUD_TYPE;
            }else if(resAccDTO.getPosition().equals("PROF")){
                return PROF_TYPE;
            }else{
                return ADMIN_TYPE;
            }
        }catch(IllegalArgumentException e){
            // 로그인 실패 - 실패 메시지 전송
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
        return USER_UNDEFINED;
    }

    // 관리자 계정 생성
    private void createAdmin(Protocol recvPt) throws Exception{
        AdminAppService adminService = new AdminAppService(adminRepo, accRepo);
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            if(recvPt.getEntity() != Protocol.ENTITY_ADMIN){
                throw new IllegalArgumentException("교직원만 생성가능");
            }
            // 클라이언트로부터 받은 adminDTO로 관리자 계정 생성
            AdminDTO dto = (AdminDTO) recvPt.getObject();
            adminService.create(dto);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            // 계정 생성 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
    }

    // 로그아웃
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }
}

