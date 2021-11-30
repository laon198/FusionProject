package controller;

import application.AccountAppService;
import application.AdminAppService;
import domain.repository.AccountRepository;
import domain.repository.AdminRepository;
import infra.dto.AccountDTO;
import infra.dto.AdminDTO;
import infra.dto.MessageDTO;
import infra.network.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
                case Protocol.T1_CODE_LOGIN:   // 로그인
                    return loginReq(recvPt);
                case Protocol.T1_CODE_CREATE: //admin 생성
                    createAdmin(recvPt);
                    return USER_UNDEFINED;
                case Protocol.T1_CODE_LOGOUT:  // 로그아웃
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

    private int loginReq(Protocol recvPt) throws Exception{
        System.out.println("login entry");
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        AccountDTO accDTO = (AccountDTO) recvPt.getObject();

        try{
            AccountAppService accService = new AccountAppService(accRepo);
            AccountDTO resAccDTO= accService.login(accDTO);

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
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }

        return USER_UNDEFINED;
    }

    private void createAdmin(Protocol recvPt) throws Exception{
        if(recvPt.getEntity()!=Protocol.ENTITY_ADMIN){
            //TODO : 처리
        }
        AdminAppService adminService = new AdminAppService(adminRepo, accRepo);
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            AdminDTO dto = (AdminDTO) recvPt.getObject();
            adminService.create(dto);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }
}

