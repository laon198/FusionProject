package domain.service;

import domain.generic.LectureTime;
import domain.generic.Period;
import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.StudentRepository;
import infra.database.SimpleCourseRepository;
import infra.database.SimpleLectureRepository;
import infra.database.SimpleStudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisterServiceTest {
    public static LectureRepository lectureRepo;
    public static CourseRepository courseRepo;
    public static StudentRepository stdRepo;
    public static Registrar registerService;

    @BeforeAll
    public static void setUp(){
        lectureRepo = new SimpleLectureRepository();
        courseRepo = new SimpleCourseRepository();
        stdRepo = new SimpleStudentRepository();
    }

    @BeforeEach
    public void init() throws Exception {
        registerService = new Registrar(lectureRepo, courseRepo, stdRepo);
        Professor p1 = Professor.builder()
                .id(1)
                .name("kim")
                .department("SE")
                .birthDate("19801112")
                .build();

        Course c1 = new Course(1, "SE0002", "융합프로젝트", "SE", 2, 3);
        Course c2 = new Course(2, "SE0003", "과기독", "SE", 2, 3);
        Course c3 = new Course(3, "SE0004", "운영체제", "SE", 2, 3);
        Course c4 = new Course(4, "SE0005", "자료구조", "SE", 2, 21);
        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);

        Set<LectureTime> time1 = new HashSet<>();
        time1.add(
                new LectureTime(
                        LectureTime.DayOfWeek.MON,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l1 = new Lecture(
                1,
                p1.getID(),
                2,
                c1.getId(),
                time1
        );

        Set<LectureTime> time2 = new HashSet<>();
        time2.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l2 = new Lecture(
                2,
                p1.getID(),
                2,
                c2.getId(),
                time2
        );

        Set<LectureTime> time3 = new HashSet<>();
        time3.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l3 = new Lecture(
                3,
                p1.getID(),
                2,
                c2.getId(),
                time3
        );


        Lecture l4 = new Lecture(
                4,
                p1.getID(),
                2,
                c3.getId(),
                time3
        );

        Set<LectureTime> time4 = new HashSet<>();
        time4.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        Lecture l5 = new Lecture(
                5,
                p1.getID(),
                2,
                c3.getId(),
                time4
        );

        Set<LectureTime> time5 = new HashSet<>();
        time5.add(
                new LectureTime(
                        LectureTime.DayOfWeek.WED,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.THIRD,
                        "D123"
                )
        );

        Lecture l6 = new Lecture(
                6,
                p1.getID(),
                2,
                c4.getId(),
                time5
        );

        lectureRepo.save(l1);
        lectureRepo.save(l2);
        lectureRepo.save(l3);
        lectureRepo.save(l4);
        lectureRepo.save(l5);
        lectureRepo.save(l6);

        Student std1 = Student.builder()
                .id(1)
                .year(1)
                .name("kim")
                .department("컴소공")
                .birthDate("19990329")
                .build();
        Student std2 = Student.builder()
                .id(2)
                .year(1)
                .name("lee")
                .department("컴소공")
                .birthDate("19990329")
                .build();
        Student std3 = Student.builder()
                .id(3)
                .year(1)
                .name("lee")
                .department("컴소공")
                .birthDate("19990329")
                .build();
        Student std4 = Student.builder()
                .id(4)
                .year(1)
                .name("lee")
                .department("컴소공")
                .birthDate("19990329")
                .build();
        Student std5 = Student.builder()
                .id(5)
                .year(2)
                .name("lee")
                .department("컴소공")
                .birthDate("19990329")
                .build();

        stdRepo.save(std1);
        stdRepo.save(std2);
        stdRepo.save(std3);
        stdRepo.save(std4);
        stdRepo.save(std5);

        Set<Student.Year> allowedYears = new HashSet<>();
        allowedYears.add(Student.Year.FRESHMAN);
        allowedYears.add(Student.Year.SENIOR);


        registerService.addRegisteringPeriod(
                new RegisteringPeriod(
                        new Period(
                                LocalDateTime.of(2021,03,01,12,00,00),
                                LocalDateTime.of(2021,12,01,12,00,00)
                        ),
                        allowedYears
                )
        );
    }

    @DisplayName("수강신청 성공 테스트")
    @Test
    public void registerSuccessTest() throws Exception {
        registerService.register(1, 1);
    }

    @DisplayName("수강인원초과 실패 테스트")
    @Test
    public void exceedLimitStdFailTest(){
        registerService.register(1, 1);
        registerService.register(1, 2);
        registerService.register(1, 3);

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(1, 4);
        });

        assertEquals("수강인원이 초과 되었습니다", exception.getMessage());
    }

    @DisplayName("중복강의 실패 테스트")
    @Test
    public void duplicatedCourseFailTest(){

        registerService.register(2, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(3, 1);
        });

        assertEquals("중복된 수강입니다.", exception.getMessage());
    }

    @DisplayName("중복시간 실패 테스트 - 완전히 같은시간겹치는경우")
    @Test
    public void allDuplicatedTimeFailTest(){

        registerService.register(3, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(4, 1);
        });

        assertEquals("같은시간에 강의를 수강중입니다.", exception.getMessage());
    }

    @DisplayName("중복시간 실패 테스트 - 부분 시간겹치는경우")
    @Test
    public void partitionDuplicatedTimeFailTest(){

        registerService.register(4, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(5, 1);
        });

        assertEquals("같은시간에 강의를 수강중입니다.", exception.getMessage());
    }

    @DisplayName("학점초과 실패 테스트")
    @Test
    public void exceedCreditFailTest(){

        registerService.register(1, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.register(6, 1);
        });

        assertEquals("수강할수 있는 학점을 초과했습니다.", exception.getMessage());
    }

    @DisplayName("수강안하는 강의 수강취소 실패 테스트")
    @Test
    public void notRegisteredLectureCancelFailTest(){
//        registerService.register(1, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {
            registerService.cancel(1, 1);
        });

        assertEquals("수강하지 않는 강의 입니다.", exception.getMessage());
    }

    @DisplayName("수강취소 성공 테스트")
    @Test
    public void cancelSuccessTest(){

        registerService.register(1, 1);
        registerService.cancel(1, 1);
    }

    @DisplayName("수강기간X 수강신청 실패 - 수강기간 아닐때")
    @Test
    public void notRegisteringPeriodFailTest(){
        Set<Student.Year> allowedYears = new HashSet<>();
        allowedYears.add(Student.Year.SOPHOMORE);

        registerService.addRegisteringPeriod(
                new RegisteringPeriod(
                        new Period(
                                LocalDateTime.of(2021,03,01,12,00,00),
                                LocalDateTime.of(2021,04,01,12,00,00)
                        ),
                        allowedYears
                )
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(1, 5);
        });

        assertEquals("해당학년 수강신청 기간이 아닙니다.", exception.getMessage());
    }

    @DisplayName("수강기간X 수강신청 실패 - 해당학년 아닐때")
    @Test
    public void notRegisteringPeriodAboutStdFailTest(){
        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> {
            registerService.register(1, 5);
        });

        assertEquals("해당학년 수강신청 기간이 아닙니다.", exception.getMessage());

    }
}