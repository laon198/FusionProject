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

public class AdminController implements DefinedController {
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

        this.regService = regService;
        stdService = new StudentAppService(stdRepo, accRepo, regRepo);
        profService = new ProfessorAppService(profRepo, accRepo);
        adminService = new AdminAppService(adminRepo, accRepo);
        courseService = new CourseAppService(courseRepo);
        lectureService = new LectureAppService(lectureRepo, courseRepo, profRepo, plannerPeriodRepo);
    }

    @Override
    public int handler(Protocol recvPt) throws Exception  {
        switch (recvPt.getCode()) {
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
            case Protocol.T1_CODE_LOGOUT:
                logoutReq();
                return USER_UNDEFINED;
            default:
        }
        return ADMIN_TYPE;
    }

    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }

    // 생성 요청 받았을 때 수행할 일
    private void createReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT:
                createStudent(recvPt);
                break;
            case Protocol.ENTITY_PROFESSOR:
                createProfessor(recvPt);
                break;
            case Protocol.ENTITY_COURSE:
                createCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:
                createLecture(recvPt);
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                createRegisteringPeriod(recvPt);
                break;
            case Protocol.ENTITY_PLANNER_PERIOD:
                createPlannerPeriod(recvPt);
                break;
            default:
        }
    }

    // 조회 요청
    private void readReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT:
                readStudent(recvPt);
                break;
            case Protocol.ENTITY_PROFESSOR:
                readProf(recvPt);
                break;
            case Protocol.ENTITY_COURSE:  // 교과목 조회
                readCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 조회
                readLecture(recvPt);
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                readRegPeriod(recvPt);
                break;
            case Protocol.ENTITY_PLANNER_PERIOD:
                readPlannerPeriod(recvPt);
                break;
            default:
        }
    }

    // 변경 요청
    private void updateReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity())
        {
            case Protocol.ENTITY_STUDENT: // 계정정보 변경
                updateStudent(recvPt);
                break;
            case Protocol.ENTITY_PROFESSOR:
                updateProfessor(recvPt);
                break;
            case Protocol.ENTITY_COURSE:  // 교과목 변경
                updateCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 변경
                updateLecture(recvPt);
                break;
            default:
        }
    }

    // 삭제 요청
    private void deleteReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_COURSE:  // 교과목 삭제
                deleteCourse(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // 개설교과목 삭제
                deleteLecture(recvPt);
                break;
            case Protocol.ENTITY_REGIS_PERIOD:
                deleteRegPeriod(recvPt);
                break;
            default:
        }
    }

    /*
     < 계정 생성 >
     */
    private void createStudent(Protocol recvPt) throws Exception {
        StudentDTO stdDTO = (StudentDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try{
            stdService.create(stdDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 계정 생성 실패 - 중복 계정 존재
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void createProfessor(Protocol recvPt) throws Exception {
        ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try{
            profService.create(profDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 계정 생성 실패 - 중복 계정 존재
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
    < 교과목 생성 >
     */
    private void createCourse(Protocol recvPt) throws Exception {
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            courseService.create(courseDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 교과목 생성 실패 - 중복 교과목 존재
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    //< 개설 교과목 생성 >
    private void createLecture(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            lectureService.create(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);  // 성공
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
    < 수강신청기간 생성 >
     */
    private void createRegisteringPeriod(Protocol recvPt) throws Exception {
        RegisteringPeriodDTO regPeriodDTO = (RegisteringPeriodDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            regService.addRegisteringPeriod(regPeriodDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            // 수강신청기간 등록 실패 - 중복 기간 존재 (시작, 끝 날짜가 완전히 중복)
             sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
    < 강의계획서 입력 기간 설정 >
     */
    private void createPlannerPeriod(Protocol recvPt) throws Exception {
        PeriodDTO periodDTO = (PeriodDTO) recvPt.getObject();
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            lectureService.changePlannerPeriod(periodDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch (IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }


    private void readStudent(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{
                try{
                    StudentDTO[] res = stdService.retrieveAll();
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
                    StudentDTO stdDTO = (StudentDTO) recvPt.getObject();
                    StudentDTO res = stdService.retrieveByID(stdDTO.getId());
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
                    StudentOption[] options = (StudentOption[]) recvPt.getObjectArray();
                    StudentDTO[] res = stdService.retrieveByOption(options);
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

    /*
    < 교과목 조회 (전체) >
    - createLecture 할 때 필요
     */
    private void readCourse(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{
                try{
                    CourseDTO[] res = courseService.retrieveAll();
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
                    CourseDTO courseDTO = (CourseDTO) recvPt.getObject();
                    CourseDTO res = courseService.retrieveByID(courseDTO.getId());
                    sendPt.setObject(res);
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    /*
    < 개설 교과목 정보 조회 >
     */
    private void readLecture(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{
                try{
                    LectureDTO[] res = lectureService.retrieveAll();
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
                    LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
                    LectureDTO res = lectureService.retrieveByID(lectureDTO.getId());
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
                    LectureOption[] options = (LectureOption[]) recvPt.getObjectArray();
                    LectureDTO[] res = lectureService.retrieveByOption(options);
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

    private void readRegPeriod(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            RegisteringPeriodDTO[] regPeriods = regService.retrieveRegPeriodAll();
            sendPt.setObjectArray(regPeriods);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
    }

    private void readPlannerPeriod(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            PeriodDTO periodDTO = lectureService.retrievePlannerPeriod();
            sendPt.setObject(periodDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.setObject(new MessageDTO(e.getMessage()));
            sendPt.send(os);
        }
    }



    /*
     < 개인정보(전화번호) 수정 및 비밀번호 수정 >
     */
    private void updateStudent(Protocol recvPt) throws Exception {
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

    private void updateProfessor(Protocol recvPt) throws Exception {
        ProfessorDTO profDTO = (ProfessorDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            profService.update(profDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }


    /*
     < 교과목 수정 >
     클라이언트
      - 교과목 조회 (readCourse)
      - 관리자가 교과목 선택 -> 교과목 pk 및 변경할 내용을 담은 패킷 전송
     */
    private void updateCourse(Protocol recvPt) throws Exception {
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            courseService.update(courseDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
    < 개설교과목 수정 >
     클라이언트
     - 개설교과목 조회 (readLecture)
     - 관리자가 개설교과목 선택 -> 개설교과목 pk 및 변경할 내용을 담은 패킷 전송
    */
    private void updateLecture(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            lectureService.update(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
    < 교과목 삭제 >
    클라이언트
    - 교과목 목록 조회 (readCourse)
    - 관리자가 교과목 선택 -> 교과목 pk 전송
    */
    private void deleteCourse(Protocol recvPt) throws Exception {
        CourseDTO courseDTO = (CourseDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            courseService.delete(courseDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    /*
     < 개설교과목 삭제 >
     클라이언트
     - 개설교과목 목록 조회 (readLecture)
     - 관리자가 개설교과목 선택 -> 교과목 pk 전송
    */
    private void deleteLecture(Protocol recvPt) throws Exception {
        LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            lectureService.delete(lectureDTO);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

    private void deleteRegPeriod(Protocol recvPt) throws Exception {
        RegisteringPeriodDTO regPeriod = (RegisteringPeriodDTO) recvPt.getObject();

        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        try {
            regService.removeRegisteringPeriod(regPeriod);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.send(os);

        } catch (IllegalArgumentException e) { // 삭제 실패 (존재하지 않는 pk) - 이런 경우가 있을진 모르겠음
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }

}

