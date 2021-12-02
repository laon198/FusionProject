package controller;

import application.*;
import domain.model.Lecture;
import domain.model.RegisteringPeriod;
import domain.repository.*;
import infra.database.option.lecture.LectureCodeOption;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorOption;
import infra.database.option.student.StudentOption;
import infra.dto.*;
import infra.network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AdminController implements DefinedController { // 관리자 controller
    public static final int USER_UNDEFINED = 0;
    public static final int ADMIN_TYPE = 3;

    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;
    private final PlannerPeriodRepository plannerPeriodRepo;

    private final StudentAppService stdService;
    private final ProfessorAppService profService;
    private final AdminAppService adminService;
    private final CourseAppService courseService;
    private final LectureAppService lectureService;
    private final RegisterAppService regService;

    private final InputStream is;
    private final OutputStream os;

    public AdminController(
            AccountRepository accRepo, AdminRepository adminRepo,
            CourseRepository courseRepo, LectureRepository lectureRepo,
            ProfessorRepository profRepo, RegisteringRepository regRepo,
            RegPeriodRepository regPeriodRepo, StudentRepository stdRepo,
            PlannerPeriodRepository plannerPeriodRepo,
            RegisterAppService regService,
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
        this.is = is;
        this.os = os;

        //각 기능을 수행할 (서비스)객체
        this.regService = regService;
        stdService = new StudentAppService(stdRepo, accRepo, regRepo);
        profService = new ProfessorAppService(profRepo, accRepo);
        adminService = new AdminAppService(adminRepo, accRepo);
        courseService = new CourseAppService(courseRepo);
        lectureService = new LectureAppService(lectureRepo, courseRepo, profRepo, plannerPeriodRepo);
    }

    @Override
    public int handler(Protocol recvPt) throws Exception  {
        // recvPt는 클라이언트로부터 받은 packet
        switch (recvPt.getCode()) { // code로 분류
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
            case Protocol.T1_CODE_LOGOUT:   // 로그아웃
                logoutReq();
                return USER_UNDEFINED;  // 로그아웃요청이 왔을 땐 userType을 USER_UNDEFINED로 지정
            default:
        }
        return ADMIN_TYPE;
    }

    // 로그아웃 요청이 왔을 때 수행할 일
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }

    // 생성 요청 받았을 때 수행할 일
    private void createReq (Protocol recvPt) throws Exception {
        // entity로 분류
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT:
                createStudent(recvPt);  // 학생생성 요청
                break;
            case Protocol.ENTITY_PROFESSOR:
                createProfessor(recvPt);  // 교수생성 요청
                break;
            case Protocol.ENTITY_COURSE:
                createCourse(recvPt);   // 교과목 생성 요청
                break;
            case Protocol.ENTITY_LECTURE:
                createLecture(recvPt);  // 개설교과목 생성 요청
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                createRegisteringPeriod(recvPt);    // 수강신청기간 설정 요청
                break;
            case Protocol.ENTITY_PLANNER_PERIOD:
                createPlannerPeriod(recvPt);    // 강의계획서 입력 기간 설정 요청
                break;
            default:
        }
    }

    // 조회 요청
    private void readReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT:
                readStudent(recvPt);    // 학생 조회 요청
                break;
            case Protocol.ENTITY_PROFESSOR: 
                readProf(recvPt);       // 교수 조회 요청
                break;
            case Protocol.ENTITY_COURSE:  
                readCourse(recvPt);     // 교과목 조회 요청
                break;
            case Protocol.ENTITY_LECTURE: 
                readLecture(recvPt);    // 개설교과목 조회 요청
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                readRegPeriod();  // 수강신청기간 조회 요청
                break;
            case Protocol.ENTITY_PLANNER_PERIOD:
                readPlannerPeriod();  // 강의계획서 입력기간 조회 요청
                break;
            default:
        }
    }

    // 변경 요청
    private void updateReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_STUDENT:
                updateStudent(recvPt);  // 학생 정보 수정 요청
                break;
            case Protocol.ENTITY_PROFESSOR:
                updateProfessor(recvPt);    // 교수 정보 수정 요청
                break;
            case Protocol.ENTITY_COURSE:
                updateCourse(recvPt);   // 교과목 정보 수정 요청
                break;
            case Protocol.ENTITY_LECTURE:  
                updateLecture(recvPt);  // 개설교과목 정보 수정 요청
                break;
            default:
        }
    }

    // 삭제 요청
    private void deleteReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_COURSE:  
                deleteCourse(recvPt);   // 교과목 삭제 요청
                break;
            case Protocol.ENTITY_LECTURE:  
                deleteLecture(recvPt);  // 개설교과목 삭제 요청
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                deleteRegPeriod(recvPt);     // 수강신청기간 삭제 요청
                break;
            default:
        }
    }

    // 학생 계정 생성 요청왔을 떄 수행할 일
    private void createStudent(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 studentDTO
        StudentDTO stdDTO = (StudentDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE); // 응답 패킷 생성
        
        try{
            stdService.create(stdDTO);  // 학생 계정 생성
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);    // 클라이언트에게 응답 패킷 전송
        }catch (IllegalArgumentException e){
            // 계정 생성 실패 - 중복 계정 존재
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 교수 생성
    private void createProfessor(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 professorDTO
        ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        
        try{
            profService.create(profDTO);    // 교수 계정 생성
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 계정 생성 실패 - 중복 계정 존재
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 교과목 생성
    private void createCourse(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 courseDTO
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        
        try{
            courseService.create(courseDTO);    // 교과목 생성
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 교과목 생성 실패 - 중복 교과목 존재
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 개설 교과목 생성
    private void createLecture(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 lectureDTO
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            lectureService.create(lectureDTO);  // 개설교과목 생성
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 개설교과목 생성 실패 - 중복 개설교과목 존재
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 수강신청기간 설정
    private void createRegisteringPeriod(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 registeringPeriodDTO
        RegisteringPeriodDTO regPeriodDTO = (RegisteringPeriodDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            regService.addRegisteringPeriod(regPeriodDTO);  // 수강신청기간 생성
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 강의계획서 입력 기간 생성
    private void createPlannerPeriod(Protocol recvPt) throws Exception {
        // 클라이언트로부터 받은 periodDTO
        PeriodDTO periodDTO = (PeriodDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            lectureService.changePlannerPeriod(periodDTO);  // 강의계획서 입력 기간 설정
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 학생 조회
    private void readStudent(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // 전체 조회
                try{
                    // 학생 전체 조회하여 StudentDTO 배열에 담아 클라이언트에게 학생 정보 전송
                    StudentDTO[] res = stdService.retrieveAll();    
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 학생이 존재하지 않을 경우 - 실패 메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{  // ID 조회
                try{
                    // 클라이언트에게 studentDTO를 받아 id 추출하여 학생 조회, 클라이언트에게 학생 정보 전송
                    StudentDTO stdDTO = (StudentDTO) recvPt.getObject();
                    StudentDTO res = stdService.retrieveByID(stdDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 학생이 존재하지 않을 경우 - 실패 메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION:{  // 옵션 조회
                try{
                    // 클라이언트에게 StudnetOption배열 받아 옵션으로 학생 조회, 클라이언트에게 학생정보 전송
                    StudentOption[] options = (StudentOption[]) recvPt.getObjectArray();
                    StudentDTO[] res = stdService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 학생이 존재하지 않을 경우 - 실패 메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // 교수 조회
    private void readProf(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{
                try{
                    ProfessorDTO[] res = profService.retrieveAll();
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{
                try{
                    ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();
                    ProfessorDTO res = profService.retrieveByID(profDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION:{
                try{
                    ProfessorOption[] options = (ProfessorOption[]) recvPt.getObjectArray();
                    ProfessorDTO[] res = profService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // 교과목 조회
    private void readCourse(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // 전체조회
                try{
                    CourseDTO[] res = courseService.retrieveAll();
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 교과목 존재하지 않을 경우 실패 메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{  // id로 조회
                try{
                    CourseDTO courseDTO = (CourseDTO) recvPt.getObject();
                    CourseDTO res = courseService.retrieveByID(courseDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 교과목 존재하지 않을 경우 실패 메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // 개설 교과목 정보 조회
    private void readLecture(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // 전체 조회
                try{
                    LectureDTO[] res = lectureService.retrieveAll();
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 생성된 개설교과목 존재하지 않을 경우 - 실패메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{      // id로 조회
                try{
                    LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
                    LectureDTO res = lectureService.retrieveByID(lectureDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 생성된 개설교과목 존재하지 않을 경우 - 실패메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION:{  // 옵션으로 조회
                try{
                    LectureOption[] options = (LectureOption[]) recvPt.getObjectArray();
                    LectureDTO[] res = lectureService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // 생성된 개설교과목 존재하지 않을 경우 - 실패메시지 전송
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // 수강신청 기간 조회 요청
    private void readRegPeriod() throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            RegisteringPeriodDTO[] regPeriods = regService.retrieveRegPeriodAll();
            sendPt.setObjectArray(regPeriods);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            // 등록된 수강신청기간 존재하지 않을 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
    }

    // 강의계획서 입력기간 조회 요청
    private void readPlannerPeriod() throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            PeriodDTO periodDTO = lectureService.retrievePlannerPeriod();
            sendPt.setObject(periodDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            // 등록된 강의계획서 입력기간 존재하지 않을 경우
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
    }

    // 개인정보(전화번호) 수정 및 비밀번호 수정
    private void updateStudent(Protocol recvPt) throws Exception {
        StudentDTO stdDTO = (StudentDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            stdService.update(stdDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { 
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void updateProfessor(Protocol recvPt) throws Exception {
        ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            profService.update(profDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }


    //  교과목 수정
    private void updateCourse(Protocol recvPt) throws Exception {
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            courseService.update(courseDTO);    // 교과목 수정
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { 
            // 교과목 수정 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 개설교과목 수정
    private void updateLecture(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            lectureService.update(lectureDTO);      // 개설교과목 수정
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { 
            // 개설교과목 수정 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

   //  교과목 삭제 
    private void deleteCourse(Protocol recvPt) throws Exception {
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            courseService.delete(courseDTO);    // 교과목 삭제
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { 
            // 교과목 삭제 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

     // 개설교과목 삭제
    private void deleteLecture(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            lectureService.delete(lectureDTO);      // 개설교과목 삭제
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            // 교과목 삭제 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    // 수강신청기간 삭제
    private void deleteRegPeriod(Protocol recvPt) throws Exception {
        RegisteringPeriodDTO regPeriod = (RegisteringPeriodDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            regService.removeRegisteringPeriod(regPeriod);  // 수강신청기간 삭제
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) {
            // 수강신청기간 삭제 실패
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

}

