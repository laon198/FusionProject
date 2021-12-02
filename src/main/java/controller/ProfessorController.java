package controller;
import application.AccountAppService;
import application.LectureAppService;
import application.ProfessorAppService;
import application.StudentAppService;
import domain.repository.*;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorOption;
import infra.dto.*;
import infra.network.Protocol;

import java.io.IOException;
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
    private final AccountAppService accService;

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

        //각 기능을 수행하는 서비스
        profService = new ProfessorAppService(profRepo, accRepo);
        lectureService = new LectureAppService(lectureRepo, courseRepo, profRepo, plannerPeriodRepo);
        stdService = new StudentAppService(stdRepo, accRepo, regRepo);
        accService = new AccountAppService(accRepo);

        this.is = is;
        this.os = os;
    }

    @Override
    public int handler(Protocol recvPt) throws Exception {
        switch (recvPt.getCode()) {
            case Protocol.T1_CODE_READ:   // 조회요청
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // 변경요청
                updateReq(recvPt);
                break;
            case Protocol.T1_CODE_LOGOUT:   // 로그아웃요청
                logoutReq();
                return USER_UNDEFINED;
            default:
        }
        return PROF_TYPE;
    }

    // 로그아웃 요청이 왔을 때 수행할 일
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }

    // 조회 요청
    private void readReq (Protocol recv) throws Exception {
        switch (recv.getEntity()) {
            case Protocol.ENTITY_PROFESSOR: // 개인정보 조회 요청
                readProfessor(recv);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회 요청
                readLecture(recv);
                break;
            case Protocol.ENTITY_REGISTRATION:  // 수강신청학생목록 조회 요청
                readLectureStudList(recv);
                break;
            default:
        }
    }

    // 변경 요청
    private void updateReq (Protocol recv) throws Exception {
        switch (recv.getEntity()) {
            case Protocol.ENTITY_ACCOUNT: // 비밀번호 변경 요청
                changePassword(recv);
                break;
            case Protocol.ENTITY_PROFESSOR: // 개인정보 변경 요청
                updateProfessor(recv);
                break;
            case Protocol.ENTITY_LECTURE:   // 개설교과목(강의게획서) 변경 요청
                updateLecturePlanner(recv);
                break;
            default:
        }
    }

    // 개인정보 조회 
    private void readProfessor(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch (recvPt.getReadOption()) {
            case Protocol.READ_BY_ID: { // 교수의 id로 조회
                try {
                    // 해당 교수의 id로 개인정보 조회하여 응답 
                    ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();
                    ProfessorDTO res = profService.retrieveByID(profDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                } catch (IllegalArgumentException e) {
                    // 개인정보 조회 실패
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // 개설교과목 조회
    private void readLecture(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // 전체조회 요청일 때
                try{
                    // 전체 개설교과목 목록 조회하여 응답
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    LectureDTO[] res = lectureService.retrieveAll();
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 개설교과목 존재하지 않아 실패 응답
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{  // id로 조회
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
            case Protocol.READ_BY_OPTION:{      // 옵션으로 조회
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

    // 개설교과목 수강신청 학생목록 조회
    private void readLectureStudList(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
            StudentDTO[] res = stdService.retrieveRegisteringStd(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(res);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 수강신청 학생목록 없을 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 비밀번호 수정
    private void changePassword(Protocol recvPt) throws Exception {
        AccountDTO accDTO = (AccountDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            accService.changePassword(accDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);

        }
    }

    // 개인정보 수정
    private void updateProfessor(Protocol recv) throws Exception {
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

    // 강의계획서 수정
    private void updateLecturePlanner(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            lectureService.update(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

}
