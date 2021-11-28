package controller;

import application.AccountAppService;
import application.RegisterAppService;
import application.StudentAppService;
import domain.repository.*;
import infra.dto.AccountDTO;
import infra.dto.RegisteringDTO;
import infra.dto.StudentDTO;
import infra.network.Protocol;
import java.io.InputStream;
import java.io.OutputStream;

public class StudentController implements DefinedController {
    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;

    private InputStream is;
    private OutputStream os;

    public StudentController(
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
    public void handler(Protocol recvPt) throws Exception {
        switch (recvPt.getCode()){
            case Protocol.T1_CODE_CREATE: // 등록
                createReq(recvPt);
                break;
            case Protocol.T1_CODE_READ:   // 조회
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // 변경
                updateReq(recvPt);
                break;
            case Protocol.T1_CODE_DELETE:  // 삭제
                deleteReq(recvPt);
                break;
            default:
        }
    }


    // 생성 요청
    private void createReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()){
            case Protocol.ENTITY_REGISTRATION:
                createRegistration(recvPt);
                break;
            default:
        }
    }

    // 조회 요청
    private void readReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT: // 학생 조회
                readStudent(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture();
                break;
            case Protocol.ENTITY_REGIS_PERIOD: // 수강신청기간 조회
                readRegisteringPeriod();
                break;
            default:
        }
    }

    // 변경 요청
    private void updateReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_ACCOUNT: // 비밀번호 변경
                changePassword(recvPt);
                break;
            case Protocol.ENTITY_STUDENT:
                updateStudent(recvPt);
                break;
            default:
        }
    }

    // 삭제 요청
    private void deleteReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_REGISTRATION:  // 수강신청 취소
                deleteRegistration(recvPt);
                break;
            default:
        }
    }

    /*
     < 수강신청 >
     클라이언트
     - 수강신청 전 수강신청기간 조회하여 수강신청 가능한지 확인
     - 학생이 수강신청할 개설교과목 선택(수강신청기간에 해당하는 교과목만 출력 및 선택 가능)
     - 수강신청 요청 메시지 전송
     */
    private void createRegistration(Protocol recvPt) throws Exception {
        RegisterAppService regService = new RegisterAppService(
                lectureRepo, stdRepo, courseRepo,
                regRepo, regPeriodRepo
        );
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);

        try{
            RegisteringDTO regDTO = (RegisteringDTO) recvPt.getObject();
            regService.register(regDTO);
            sendPt.send(os);
        }catch(Exception e){
            // 실패 - 수강신청인원초과 or 시간표중복
            // sendPt.setCode(Protocol.T2_CODE_FAIL);
        }
    }

    /*
    < 학생 조회 >
     */
    private void readStudent(Protocol recvPt) throws Exception {
        // 개인정보 조회 기능 수행
        Object sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObject(sndData);

        //sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 개설교과목 조회 (전학년) >
     */
    private void readLecture() throws Exception {
        // 개설교과목 전체 조회 기능 수행
        Object[] sndData = null;
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.setObjectArray(sndData);
        // 개설교과목 조회 실패 - 존재하는 개설교과목 없음
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);

    }

    /*
    < 수강신청기간 조회 >
    createRegistration 할 때 필요
    수강신청기간을 보내주는 것이 아니라 현재 날짜가 수강신청 가능한 날짜인지 yes(success)/no(fail)로 답변
     */
    private void readRegisteringPeriod() throws Exception {
        // 수강신청 기간 조회 기능 수행
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        // if (수강신청기간임)
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        // else
        // sendPt.setCode(Protocol.T2_CODE_FAIL);
        sendPt.send(os);
    }

    /*
     < 비밀번호 수정 >
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

    private void updateStudent(Protocol recvPt) throws Exception {
        StudentAppService stdService = new StudentAppService(stdRepo, accRepo);
        StudentDTO stdDTO = (StudentDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            stdService.update(stdDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }


    /*
     < 수강신청 취소 >
     */
    private void deleteRegistration(Protocol recvPt) throws Exception {
        RegisterAppService regService = new RegisterAppService(
                lectureRepo, stdRepo, courseRepo,
                regRepo, regPeriodRepo
        );
        RegisteringDTO regDTO = (RegisteringDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try {
            regService.cancel(regDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }
}
