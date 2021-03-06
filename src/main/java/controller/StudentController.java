package controller;

import application.AccountAppService;
import application.LectureAppService;
import application.RegisterAppService;
import application.StudentAppService;
import domain.model.Student;
import domain.repository.*;
import dto.*;
import infra.database.option.lecture.LectureOption;
import infra.database.option.student.StudentOption;
import infra.network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StudentController implements DefinedController {
    public static final int USER_UNDEFINED = 0;
    public static final int STUD_TYPE = 1;

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
    private final LectureAppService lectureService;
    private final RegisterAppService regService;

    private InputStream is;
    private OutputStream os;

    public StudentController(
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
        lectureService = new LectureAppService(lectureRepo, courseRepo, profRepo, plannerPeriodRepo);
    }

    @Override
    public int handler(Protocol recvPt) throws Exception {
        switch (recvPt.getCode()){
            case Protocol.T1_CODE_CREATE: // ??????
                createReq(recvPt);
                break;
            case Protocol.T1_CODE_READ:   // ??????
                readReq(recvPt);
                break;
            case Protocol.T1_CODE_UPDATE:  // ??????
                updateReq(recvPt);
                break;
            case Protocol.T1_CODE_DELETE:  // ??????
                deleteReq(recvPt);
                break;
            case Protocol.T1_CODE_LOGOUT:   // ????????????
                logoutReq();
                return USER_UNDEFINED;
            default:
                break;
        }

        return STUD_TYPE;
    }

    // ?????? ??????
    private void createReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()){
            case Protocol.ENTITY_REGISTRATION:  // ???????????? ??????
                createRegistration(recvPt);
                break;
            default:
                break;
        }
    }

    // ?????? ??????
    private void readReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_STUDENT: // ?????? ?????? ??????
                readStudent(recvPt);
                break;
            case Protocol.ENTITY_LECTURE:  // ??????????????? ?????? ??????
                readLecture(recvPt);
                break;
            case Protocol.ENTITY_REGIS_PERIOD: // ?????????????????? ?????? ??????
                readRegisteringPeriod(recvPt);
                break;
            case Protocol.ENTITY_REGISTRATION: // ??????????????? ???????????? ??????
                readRegisteredLectures(recvPt);
                break;
            default:
                break;
        }
    }

    // ?????? ??????
    private void updateReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_ACCOUNT: // ???????????? ?????? ??????
                changePassword(recvPt);
                break;
            case Protocol.ENTITY_STUDENT: // ???????????? ?????? ??????
                updateStudent(recvPt);
                break;
            default:
                break;
        }
    }

    // ?????? ??????
    private void deleteReq (Protocol recvPt) throws Exception {
        switch (recvPt.getEntity()) {
            case Protocol.ENTITY_REGISTRATION:  // ???????????? ??????
                deleteRegistration(recvPt);
                break;
            default:
        }
    }

    // ???????????? ????????? ?????? ??? ????????? ???
    private void logoutReq() throws IOException {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);
        sendPt.send(os);
    }


    // ????????????
    private void createRegistration(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);
        sendPt.setCode(Protocol.T2_CODE_SUCCESS);

        try{
            RegisteringDTO regDTO = (RegisteringDTO) recvPt.getObject();
            regService.register(regDTO);
            sendPt.send(os);
        }catch(IllegalArgumentException | IllegalStateException e){
             sendPt.setCode(Protocol.T2_CODE_FAIL);
             sendPt.setObject(new MessageDTO(e.getMessage()));
             sendPt.send(os);
        }
    }
    
    // ?????? ?????? ??????
    private void readStudent(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    
                sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                StudentDTO[] res = stdService.retrieveAll();
                sendPt.setObjectArray(res);
                sendPt.send(os);
            }
            break;
            case Protocol.READ_BY_ID:{
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    StudentDTO stdDTO = (StudentDTO) recvPt.getObject();
                    StudentDTO res = stdService.retrieveByID(stdDTO.getId());
                    sendPt.setObject(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION:{
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    StudentOption[] options = (StudentOption[]) recvPt.getObjectArray();
                    StudentDTO[] res = stdService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // ??????????????? ??????
    private void readLecture(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // ?????? ??????
                try{
                    LectureDTO[] res = lectureService.retrieveAll();
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // ????????? ??????????????? ?????? ?????? ?????? ?????? ??????
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_ID:{  // id??? ??????
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    LectureDTO lectureDTO = (LectureDTO) recvPt.getObject();
                    LectureDTO res = lectureService.retrieveByID(lectureDTO.getId());
                    sendPt.setObject(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
            case Protocol.READ_BY_OPTION:{  //???????????? ??????
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    LectureOption[] options = (LectureOption[]) recvPt.getObjectArray();
                    LectureDTO[] res = lectureService.retrieveByOption(options);
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.send(os);
                }
                break;
            }
        }
    }

    // ?????????????????? ?????? 
    private void readRegisteringPeriod(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        switch(recvPt.getReadOption()){
            case Protocol.READ_ALL:{    // ?????? ??????
                try{
                    sendPt.setCode(Protocol.T2_CODE_SUCCESS);
                    RegisteringPeriodDTO[] res = regService.retrieveRegPeriodAll();
                    sendPt.setObjectArray(res);
                    sendPt.send(os);
                }catch(IllegalArgumentException e){
                    // ????????? ?????? ?????? ??????
                    sendPt.setCode(Protocol.T2_CODE_FAIL);
                    sendPt.setObject(new MessageDTO(e.getMessage()));
                    sendPt.send(os);
                }
                break;
            }
            default:{
                sendPt.setCode(Protocol.T2_CODE_FAIL);
                sendPt.send(os);
                break;
            }
        }
    }

    // ??????????????? ?????? ?????? ??????
    private void readRegisteredLectures(Protocol recvPt) throws Exception {
        Protocol sendPt = new Protocol(Protocol.TYPE_RESPONSE);

        try{
            StudentDTO stdDTO = (StudentDTO) recvPt.getObject();
            Student std = stdRepo.findByID(stdDTO.getId()); // ????????? id??? ??????
            LectureDTO[] res = lectureService.getRegisteredLectures(std);
            sendPt.setCode(Protocol.T2_CODE_SUCCESS);
            sendPt.setObjectArray(res);
            sendPt.send(os);
        }catch(IllegalArgumentException e){
            // ??????????????? ?????? ?????? ??????
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }


    // < ???????????? ?????? >
    private void changePassword(Protocol recvPt) throws Exception {
        AccountAppService accService = new AccountAppService(accRepo);
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


    /*
     < ???????????? ?????? >
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
        } catch (IllegalArgumentException e) {
            sendPt.setCode(Protocol.T2_CODE_FAIL);
            sendPt.send(os);
        }
    }
}
