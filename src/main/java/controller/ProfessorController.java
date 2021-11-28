package controller;
import application.AccountAppService;
import application.ProfessorAppService;
import domain.repository.*;
import infra.dto.AccountDTO;
import infra.dto.ProfessorDTO;
import infra.network.Protocol;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfessorController implements DefinedController {
    public static final int USER_UNDEFINED = 0;
    public static final int PROF_TYPE = 2;

    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;

    private final InputStream is;
    private final OutputStream os;

    public ProfessorController(
            AccountRepository accRepo, AdminRepository adminRepo,
            CourseRepository courseRepo, LectureRepository lectureRepo,
            ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo,
            InputStream is, OutputStream os){
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
        this.courseRepo = courseRepo;
        this.lectureRepo = lectureRepo;
        this.profRepo = profRepo;
        this.regRepo = regRepo;
        this.regPeriodRepo = regPeriodRepo;
        this.stdRepo = stdRepo;
        this.is = is;
        this.os = os;
    }

    @Override
    public int handler(Protocol recvPt) throws Exception {
        switch (recvPt.getCode()) {
            case Protocol.T1_CODE_READ:   // 조회
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // 변경
                updateReq(recvPt);
                break;
            default:
        }

        return PROF_TYPE;
    }

    // 조회 요청
    private void readReq (Protocol recv) throws Exception {
        switch (recv.getEntity()) {
            case Protocol.ENTITY_ACCOUNT: // 계정정보 조회
                readAccount();
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture(recv);
                break;
            case Protocol.ENTITY_LECTURE_STUD_LIST:  // 수강신청학생목록 조회
                readLectureStudList(recv);
                break;
            default:
        }
    }

    // 변경 요청
    private void updateReq (Protocol recv) throws Exception {
        switch (recv.getEntity()) {
            case Protocol.ENTITY_ACCOUNT: // 비밀번호 변경
                changePassword(recv);
                break;
            case Protocol.ENTITY_PROFESSOR: // 개인정보 변경
                updateProfessor(recv);
            default:
        }
    }


    /*
    < 개인정보 조회 >
     */
    private void readAccount() throws Exception {
        /*
         개인정보 조회 기능 수행
         */
        Object sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObject(sndData);
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);
    }

    /*
    < 개설 교과목 정보 조회 >
     */
    private void readLecture(Protocol recvPt) throws Exception {
        //Object option = recvPt.getObject();  // 학년,학과,교수 option
        /*
        개설 교과목 정보 조회하는 기능 수행
         */
        //if 옵션 잘못됨
        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        Object[] sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        if (sndData != null)
        {
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(sndData);
        }
        else // 실패 - 개설 교과목 없는 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);
    }

    /*
     < 개설교과목 수강신청 학생목록 조회 >
     클라이언트에서 해당 교수의 개설 교과목 조회 -> 교수가 개설교과목 선택 -> 개설교과목 pk전송
     */
    private void readLectureStudList(Protocol recvPt) throws Exception {
        Object option = recvPt.getObject();  // 학년,학과,교수 option
        // 학생 목록 조회 기능 수행
        Object[] sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObjectArray(sndData);
        // 조회 실패
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);
    }

    /*
     < 개인정보(전화번호) 수정 및 비밀번호 수정 >
     */
    private void changePassword(Protocol recvPt) throws Exception {
        AccountAppService accService = new AccountAppService(accRepo);
        AccountDTO accDTO = (AccountDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            accService.changePassword(accDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { //TODO : 이런로직없어 현재 비밀번호 불일치
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);

        }
    }

    private void updateProfessor(Protocol recv) throws Exception {
        ProfessorAppService profService = new ProfessorAppService(profRepo, accRepo);
        ProfessorDTO profDTO = (ProfessorDTO) recv.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            profService.update(profDTO);

            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

}
