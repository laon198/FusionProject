import application.RegisterAppService;
import domain.repository.*;
import infra.database.MyBatisConnectionFactory;
import infra.database.repository.*;
import infra.network.Server;
import org.apache.ibatis.session.SqlSessionFactory;

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
    private final PlannerPeriodRepository plannerPeriodRepo;
    private final SqlSessionFactory sqlSessionFactory;
    private final RegisterAppService regService;
    private final Server mainServer;

    public LectureRegisterApp(){
        sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();
        accRepo = new RDBAccountRepository();
        adminRepo = new RDBAdminRepository();
        courseRepo = new RDBCourseRepository(sqlSessionFactory);
        profRepo = new RDBProfessorRepository();
        lectureRepo = new RDBLectureRepository(
                courseRepo, profRepo, sqlSessionFactory
        );
        regRepo = new RDBRegisteringRepository();
        regPeriodRepo = new RDBRegPeriodRepository();
        stdRepo = new RDBStudentRepository();
        plannerPeriodRepo = new RDBPlannerPeriodRepository();
        regService = new RegisterAppService(
                lectureRepo, stdRepo, courseRepo,
                regRepo, regPeriodRepo
        );
        mainServer = new Server(
                accRepo, adminRepo, courseRepo,
                lectureRepo, profRepo, regRepo,
                regPeriodRepo, stdRepo, plannerPeriodRepo,
                regService
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
