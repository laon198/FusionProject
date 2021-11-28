package controller;

import application.AccountAppService;
import com.google.protobuf.DescriptorProtos;
import domain.repository.AccountRepository;
import infra.dto.AccountDTO;
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
    private ProtocolService ps;

    public UndefinedController(OutputStream os, AccountRepository accRepo){
        this.accRepo = accRepo;
        ps = new ProtocolService(os);
    }

    public int handler(Protocol recvPt) {
        try{
            switch (recvPt.getCode()) {
                case Protocol.T1_CODE_LOGIN:   // 로그인
                    return loginReq(recvPt);
                case Protocol.T1_CODE_CREATE:  // 관리자 계정 생성
                    createAdmin(recvPt);
                    return USER_UNDEFINED; //TODO 관리자 계정 생성하면 관리자 TYPE return?
                default:                       // 비정상 접근
                    ps.responseFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return USER_UNDEFINED;
    }

    private int loginReq(Protocol recvPt) throws IOException {
        AccountDTO resAccDTO = null;
        try {
            AccountDTO accDTO = (AccountDTO) recvPt.getObject();
            AccountAppService accService = new AccountAppService(accRepo);
            resAccDTO= accService.login(accDTO);
        } catch (Exception e) {   // 로그인 실패 예외 받음
            ps.responseFail();    // 로그인 실패 메시지 전송
            return USER_UNDEFINED;
        }

        ps.reponseSuccess();
        if(resAccDTO.getPosition().equals("STUD")){
            return STUD_TYPE;
        }else if(resAccDTO.getPosition().equals("PROF")){
            return PROF_TYPE;
        }else {
            return ADMIN_TYPE;
        }
    }



    private void createAdmin(Protocol recvPt){
//        if(recvPt.getEntity()!=Protocol.ENTITY_AD)
    }

}

