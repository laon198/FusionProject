import domain.repository.*;
import infra.database.repository.*;
import infra.network.Server;

import java.io.IOException;

public class LectureRegisterApp {
    private AccountRepository accRepo;
    private AdminRepository adminRepo;
    private CourseRepository courseRepo;
    private LectureRepository lectureRepo;
    private ProfessorRepository profRepo;
    private RegisteringRepository regRepo;
    private RegPeriodRepository regPeriodRepo;
    private StudentRepository stdRepo;
    private Server mainServer;

    public LectureRegisterApp(){
        accRepo = new RDBAccountRepository();
        adminRepo = new RDBAdminRepository();
        courseRepo = new RDBCourseRepository();
        lectureRepo = new RDBLectureRepository();
        profRepo = new RDBProfessorRepository();
        regRepo = new RDBRegisteringRepository();
        regPeriodRepo = new RDBRegPeriodRepository();
        stdRepo = new RDBStudentRepository();
        mainServer = new Server(
                accRepo, adminRepo, courseRepo,
                lectureRepo, profRepo, regRepo,
                regPeriodRepo, stdRepo
        );
    }

    public void run(){
        try{
            mainServer.run();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
