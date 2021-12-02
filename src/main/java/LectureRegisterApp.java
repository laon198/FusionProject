import application.RegisterAppService;
import domain.repository.*;
import infra.database.MyBatisConnectionFactory;
import infra.database.repository.*;
import infra.network.Server;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;

public class LectureRegisterApp {
    //각각의 DAO, 프로그램에서 하나 생성해서 의존성 주입함.
    //수강신청을 담당하는 regService는 동기화를 위해 프로그램에서
    //하나만 생성하여 의존성 주입함.
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
        mainServer = new Server(    // 서버 생성
                accRepo, adminRepo, courseRepo,
                lectureRepo, profRepo, regRepo,
                regPeriodRepo, stdRepo, plannerPeriodRepo,
                regService
        );
    }

    public void run(){  // 서버 실행
        try{
            mainServer.run();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
