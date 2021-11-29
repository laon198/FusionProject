package controller;
import application.AccountAppService;
import application.LectureAppService;
import application.ProfessorAppService;
import application.StudentAppService;
import domain.repository.*;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorOption;
import infra.dto.AccountDTO;
import infra.dto.LectureDTO;
import infra.dto.ProfessorDTO;
import infra.dto.StudentDTO;
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
    private final PlannerPeriodRepository plannerPeriodRepo;

    private final ProfessorAppService profService;
    private final LectureAppService lectureService;
    private final StudentAppService stdService;

    private final InputStream is;
    private final OutputStream os;

    public ProfessorController(
            AccountRepository accRepo, AdminRepository adminRepo,
            CourseRepository courseRepo, LectureRepository lectureRepo,
            ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo,
            PlannerPeriodRepository plannerPeriodRepo,
            InputStream is, OutputStream os){
        this.accRepo = accRepo;
        this.adminRepo = adminRepo;
        this.courseRepo = courseRepo;
        this.lectureRepo = lectureRepo;
        this.profRepo = profRepo;
        this.regRepo = regRepo;
        this.regPeriodRepo = regPeriodRepo;
        this.stdRepo = stdRepo;
        this.plannerPeriodRepo = plannerPeriodRepo;

        profService = new ProfessorAppService(profRepo, accRepo);
        lectureService = new LectureAppService(lectureRepo, courseRepo, profRepo, plannerPeriodRepo);
        stdService = new StudentAppService(stdRepo, accRepo, regRepo);

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
            case Protocol.ENTITY_PROFESSOR: // 계정정보 조회
                readProfessor(recv);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture(recv);
                break;
            case Protocol.ENTITY_REGISTRATION:  // 수강신청학생목록 조회
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
    private void readProfessor(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch (recvPt.getReadOption()) {
            case Protocol.READ_BY_ID: {
                try {
                    ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();
                    ProfessorDTO res = profService.retrieveByID(profDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    private void readLecture(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{
                sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                LectureDTO[] res = lectureService.retrieveAll();
                sendPt.setObjectArray(res);
                sendPt.send(os);
                break;
            }
            case Protocol.READ_BY_ID:{
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
                    LectureDTO res = lectureService.retrieveByID(lectureDTO.getId());
                    sendPt.setObject(res);
                    sendPt.send(os);
                    break;
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
            }
            case Protocol.READ_BY_OPTION:{
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    LectureOption[] options = (LectureOption[]) recvPt.getObjectArray();
                    LectureDTO[] res = lectureService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                    break;
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
            }
        }
    }

    /*
     < 개설교과목 수강신청 학생목록 조회 >
     클라이언트에서 해당 교수의 개설 교과목 조회 -> 교수가 개설교과목 선택 -> 개설교과목 pk전송
     */
    private void readLectureStudList(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
            StudentDTO[] res = stdService.retrieveRegisteringStd(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(res);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
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
