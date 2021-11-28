import domain.repository.*;
import infra.database.repository.*;
import infra.network.Server;

import java.io.IOException;

public class LectureRegisterApp {
    private final AccountRepository accRepo;
    private final AdminRepository adminRepo;
    private final CourseRepository courseRepo;
    private final LectureRepository lectureRepo;
    private final ProfessorRepository profRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository regPeriodRepo;
    private final StudentRepository stdRepo;
    private final Server mainServer;

    public LectureRegisterApp(){
        accRepo = new RDBAccountRepository();
        adminRepo = new RDBAdminRepository();
        courseRepo = new RDBCourseRepository();
        profRepo = new RDBProfessorRepository();
        lectureRepo = new RDBLectureRepository(
                courseRepo, profRepo
        );
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
