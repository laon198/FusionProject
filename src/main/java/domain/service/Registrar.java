package domain.service;

import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.RegisteringRepository;
import domain.repository.StudentRepository;
import infra.option.student.StudentCodeOption;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Registrar {
    private LectureRepository lectureRepo;
    private CourseRepository courseRepo;
    private Set<RegisteringPeriod> periodSet; //TODO : 의존성 주입필요

    public Registrar(LectureRepository lectureRepo, CourseRepository courseRepo,
                        Set<RegisteringPeriod> periodSet){
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
        this.periodSet = periodSet;
    }

    public Registering register(Lecture lecture, Student student){
        Course course = courseRepo.findByID(lecture.getCourseID());

        if(!isValidPeriodAbout(student, course)){
            throw new IllegalStateException("해당학년 수강신청 기간이 아닙니다.");
        }

        if(!lecture.validLimitNum()){
            throw new IllegalStateException("수강인원이 초과 되었습니다");
        }

        if(student.isDuplicatedTime(lecture.getLectureTimes())){
            throw new IllegalArgumentException("같은시간에 강의를 수강중입니다.");
        }

        if(!student.isValidCredit(course.getCredit())){
            throw new IllegalArgumentException("수강할수 있는 학점을 초과했습니다.");
        }

        //TODO : 학생객체가 수강강의 객체를 바로 참조하지못해서 캡슐화가 깨짐
        for(Registering registering : student.getMyRegisterings()){
            Lecture stdLecture = lectureRepo.findByID(registering.getLectureID());

            if(stdLecture.isEqualCourse(lecture)){
                throw new IllegalArgumentException("중복된 수강입니다.");
            }
        }

        Registering newReg = Registering.builder()
                                .lectureID(lecture.getID())
                                .studentCode(student.getStudentCode())
                                .registeringTime(LocalDateTime.now().toString())
                                .build();

        lecture.register(newReg);
        student.register(newReg, lecture.getLectureTimes(), course.getCredit());

        return newReg;
    }

    public Registering cancel(Registering registering, Student student, Lecture lecture){
        if(!student.hasLecture(lecture.getID())){
            throw new IllegalArgumentException("수강하지 않는 강의 입니다.");
        }
        Course course = courseRepo.findByID(lecture.getCourseID());

        student.cancel(registering, lecture.getLectureTimes(), course.getCredit());
        lecture.cancel(registering);

        return registering;
    }

    public boolean isValidPeriodAbout(Student std, Course course){
        for(RegisteringPeriod period : periodSet){
            if(period.isSatisfiedBy(std, course)){
                return true;
            }
        }

        return false;
    }
}
