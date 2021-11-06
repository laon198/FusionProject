package domain.service;

import domain.generic.LectureTime;
import domain.generic.Period;
import domain.model.*;
import domain.repository.LectureRepository;
import domain.repository.ProfessorRepository;
import infra.database.SimpleLectureRepository;
import infra.database.SimpleProfessorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LecturePlannerTest {
    public static LectureRepository lectureRepo;
    public static ProfessorRepository professorRepo;

    @BeforeAll
    public static void init(){
        lectureRepo = new SimpleLectureRepository();
        professorRepo = new SimpleProfessorRepository();
    }

    @BeforeEach
    public void setup() throws Exception {
        Professor p1 = new Professor(1);
        Professor p2 = new Professor(2);
        Professor p3 = new Professor(3);
        Professor p4 = new Professor(4);

        professorRepo.save(p1);
        professorRepo.save(p2);
        professorRepo.save(p3);
        professorRepo.save(p4);

        Set<LectureTime> time1 = new HashSet<>();
        time1.add(
                new LectureTime(
                        LectureTime.DayOfWeek.MON,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "123"
                )
        );

        Lecture l1 = new Lecture(
                1,
                p1.getId(),
                2,
                new Course(1, 3).getId(),
                time1
               );

        Set<LectureTime> time2 = new HashSet<>();
        time2.add(
                new LectureTime(
                        LectureTime.DayOfWeek.TUE,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "123"
                )
        );

        Lecture l2 = new Lecture(
                2,
                p2.getId(),
                2,
                new Course(2, 3).getId(),
                time2
                );

        Set<LectureTime> time3 = new HashSet<>();
        time3.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.THIRD,
                        LectureTime.LecturePeriod.FIFTH,
                        "123"
                )
        );

        Lecture l3 = new Lecture(
                3,
                p3.getId(),
                2,
                new Course(3, 3).getId(),
                time3
                );


        Lecture l4 = new Lecture(
                4,
                p4.getId(),
                2,
                new Course(3, 3).getId(),
                time3
                );

        Set<LectureTime> time4 = new HashSet<>();
        time4.add(
                new LectureTime(
                        LectureTime.DayOfWeek.FRI,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.FIFTH,
                        "123"
                )
        );

        Lecture l5 = new Lecture(
                5,
                p4.getId(),
                2,
                new Course(3, 3).getId(),
                time4
                );

        Set<LectureTime> time5 = new HashSet<>();
        time5.add(
                new LectureTime(
                        LectureTime.DayOfWeek.WED,
                        LectureTime.LecturePeriod.FIRST,
                        LectureTime.LecturePeriod.THIRD,
                        "123"
                )
        );

        Lecture l6 = new Lecture(
                6,
                p4.getId(),
                2,
                new Course(4, 21).getId(),
                time5
                );


        lectureRepo.save(l1);
        lectureRepo.save(l2);
        lectureRepo.save(l3);
        lectureRepo.save(l4);
        lectureRepo.save(l5);
        lectureRepo.save(l6);

    }

    @DisplayName("강의계획서 입력성공 테스트")
    @Test
    public void setItemSuccessTest(){
        Lecture l1 = lectureRepo.findByID(1);
        Professor p1 = professorRepo.findByID(1);

        l1.writePlanner("bookName", "operating system", p1.getId());
    }

    @DisplayName("강의계획서 입력실패 테스트 - 맞지않는 항목")
    @Test
    public void notExistItemFailTest(){
        Lecture l1 = lectureRepo.findByID(1);
        Professor p1 = professorRepo.findByID(1);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            l1.writePlanner("aa", "operating system", p1.getId());
        });

        assertEquals("존재하지않는 항목입니다.", exception.getMessage());
    }

    @DisplayName("강의계획서 입력 실패 테스트 - 해당강의 교수가 아닐때")
    @Test
    public void notEqualLecturerFailTest(){
        Lecture l1 = lectureRepo.findByID(1);
        Professor p2 = professorRepo.findByID(2);

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()->{
            l1.writePlanner("bookName", "operating system", p2.getId());
        });

        assertEquals("담당 교과목이 아닙니다.", exception.getMessage());
    }

    @DisplayName("강의계획서 입력 실패 테스트 - 강의계획서 입력기간 아닐때")
    @Test
    public void notPeriodFailTest(){
        Lecture l1 = lectureRepo.findByID(1);
        Professor p1 = professorRepo.findByID(1);

        l1.setPlannerWritingPeriod(new Period(
                LocalDateTime.of(2020,01,01,12,00),
                LocalDateTime.of(2020,12,31,00,00)
        ));

        IllegalStateException exception = assertThrows(IllegalStateException.class, ()->{
            l1.writePlanner("bookName", "operating system", p1.getId());
        });

        assertEquals("강의계획서 입력기간이 아닙니다.", exception.getMessage());
    }
}
