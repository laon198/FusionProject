package domain.service;

import domain.generic.LectureTime;
import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.ProfessorRepository;
import domain.repository.StudentRepository;
import infra.database.SimpleCourseRepository;
import infra.database.SimpleLectureRepository;
import infra.database.SimpleProfessorRepository;
import infra.database.SimpleStudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LectureManageServiceTest {
    public static LectureRepository lectureRepo;
    public static CourseRepository courseRepo;
    public static ProfessorRepository professorRepo;
    public static StudentRepository stdRepo;
    public static LectureManageService manageService;

    @BeforeAll
    public static void setUp(){
        lectureRepo = new SimpleLectureRepository();
        courseRepo = new SimpleCourseRepository();
        stdRepo = new SimpleStudentRepository();
        professorRepo = new SimpleProfessorRepository();
    }

    @BeforeEach
    public void init() throws Exception {
        manageService = new LectureManageService(courseRepo, professorRepo, lectureRepo);
        Professor p1 = Professor.builder()
                    .id(1)
                    .name("kim")
                    .department("SE")
                    .birthDate("19801112")
                    .build();
        Professor p2 = Professor.builder()
                .id(2)
                .name("lee")
                .department("SE")
                .birthDate("19801112")
                .build();
        Professor p3 = Professor.builder()
                .id(3)
                .name("lee a")
                .department("SE")
                .birthDate("19801112")
                .build();
        Professor p4 = Professor.builder()
                .id(4)
                .name("kim b")
                .department("SE")
                .birthDate("19801112")
                .build();

        professorRepo.save(p1);
        professorRepo.save(p2);
        professorRepo.save(p3);
        professorRepo.save(p4);

        Course c1 = new Course(1, "SE0002", "융합프로젝트", "SE", 2, 3);
        Course c2 = new Course(2, "SE0003", "과기독", "SE", 2, 3);
        Course c3 = new Course(3, "SE0004", "운영체제", "SE", 2, 3);
        Course c4 = new Course(4, "SE0005", "자료구조", "SE", 2, 21);
        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);

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
    }

    @DisplayName("강의생성 성공 테스트")
    @Test
    public void createNewLectureTest() throws Exception {
        Professor p1 = professorRepo.findByID(1);
        Course c1 = courseRepo.findByID(1);

        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.MON,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );
    }

    @DisplayName("강의생성 실패 테스트-강의 시간 중복")
    @Test
    public void duplicatedTimeFailTest(){
        Professor p1 = professorRepo.findByID(1);
        Course c1 = courseRepo.findByID(1);

        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            manageService.addLecture(
                    2,
                    c1.getId(),
                    p1.getId(),
                    30,
                    times
            );
        });

        assertEquals("이미 존재하는 시간입니다.", exception.getMessage());
    }

    @DisplayName("강의생성 실패 테스트-교수 시간표 중복")
    @Test
    public void duplicatedTimeTableFailTest(){
        Professor p1 = professorRepo.findByID(1);
        Course c1 = courseRepo.findByID(1);

        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D124"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        Set<LectureTime> times2 = new HashSet<>();
        times2.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D123"
                )
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            manageService.addLecture(
                    2,
                    c1.getId(),
                    p1.getId(),
                    30,
                    times2
            );
        });

        assertEquals("같은시간에 강의가 존재합니다.", exception.getMessage());
    }

    @DisplayName("강의삭제 성공 테스트")
    @Test
    public void deleteLectureTest(){
        Professor p1 = professorRepo.findByID(1);
        Course c1 = courseRepo.findByID(1);
        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D124"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        manageService.removeLecture(1);
    }

    @DisplayName("강의 최대수강인원 변경 테스트")
    @Test
    public void changeLimitTest() throws NoSuchFieldException, IllegalAccessException {
        Professor p1 = professorRepo.findByID(1);
        Course c1 = courseRepo.findByID(1);
        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D126"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        Lecture l1 = lectureRepo.findByID(1);


        manageService.updateAboutMaxStd(1, 3);

        Field field = l1.getClass().getDeclaredField("limit");
        field.setAccessible(true);
        int limit = (int)field.get(l1);

        assertEquals(limit, 3);
    }

    @DisplayName("강의실 변경 성공 테스트")
    @Test
    public void updateRoomTest(){

        Professor p1 = professorRepo.findByID(1);
        Professor p2 = professorRepo.findByID(2);
        Professor p3 = professorRepo.findByID(3);
        Course c1 = courseRepo.findByID(1);
        Set<LectureTime> times = new HashSet<>();
        times.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D124"
                )
        );

        manageService.addLecture(
                1,
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        Lecture l1 = lectureRepo.findByID(1);

        manageService.updateAboutRoom(1,
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D124"
                ), "D125");

        Set<LectureTime> times2 = new HashSet<>();
        times2.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D124"
                )
        );

        manageService.addLecture(
                2,
                c1.getId(),
                p2.getId(),
                30,
                times2
        );

        Set<LectureTime> times3 = new HashSet<>();
        times3.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "D125"
                )
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            manageService.addLecture(
                    3,
                    c1.getId(),
                    p3.getId(),
                    30,
                    times2
            );
        });

        assertEquals("이미 존재하는 시간입니다.", exception.getMessage());
    }
}