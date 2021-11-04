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
        Professor p1 = new Professor(
                new ProfessorID("1")
        );
        Professor p2 = new Professor(
                new ProfessorID("2")
        );
        Professor p3 = new Professor(
                new ProfessorID("3")
        );
        Professor p4 = new Professor(
                new ProfessorID("4")
        );

        professorRepo.save(p1);
        professorRepo.save(p2);
        professorRepo.save(p3);
        professorRepo.save(p4);

        Course c1 = new Course(new CourseID(1), 3);
        Course c2 = new Course(new CourseID(2), 3);
        Course c3 = new Course(new CourseID(3), 3);
        Course c4 = new Course(new CourseID(4), 21);
        courseRepo.save(c1);
        courseRepo.save(c2);
        courseRepo.save(c3);
        courseRepo.save(c4);

        Student std1 = new Student(new StudentID("1"), Student.Year.FRESHMAN);
        Student std2 = new Student(new StudentID("2"), Student.Year.FRESHMAN);
        Student std3 = new Student(new StudentID("3"), Student.Year.FRESHMAN);
        Student std4 = new Student(new StudentID("4"), Student.Year.FRESHMAN);
        Student std5 = new Student(new StudentID("5"), Student.Year.SOPHOMORE);

        stdRepo.save(std1);
        stdRepo.save(std2);
        stdRepo.save(std3);
        stdRepo.save(std4);
        stdRepo.save(std5);
    }

    @DisplayName("강의생성 성공 테스트")
    @Test
    public void createNewLectureTest() throws Exception {
        Professor p1 = professorRepo.findByID(new ProfessorID("1"));
        Course c1 = courseRepo.findByID(new CourseID(1));

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
                new LectureID("1"),
                c1.getId(),
                p1.getId(),
                30,
                times
        );
    }

    @DisplayName("강의생성 실패 테스트-강의 시간 중복")
    @Test
    public void duplicatedTimeFailTest(){
        Professor p1 = professorRepo.findByID(new ProfessorID("1"));
        Course c1 = courseRepo.findByID(new CourseID(1));

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
                new LectureID("1"),
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            manageService.addLecture(
                    new LectureID("2"),
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
        Professor p1 = professorRepo.findByID(new ProfessorID("1"));
        Course c1 = courseRepo.findByID(new CourseID(1));

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
                new LectureID("1"),
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
                    new LectureID("2"),
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
        Professor p1 = professorRepo.findByID(new ProfessorID("1"));
        Course c1 = courseRepo.findByID(new CourseID(1));
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
                new LectureID("1"),
                c1.getId(),
                p1.getId(),
                30,
                times
        );

        manageService.removeLecture(new LectureID("1"));
    }
}