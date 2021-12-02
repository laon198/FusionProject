package domain.service;

import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;

import java.time.LocalDateTime;
import java.util.Set;

//수강신청과 관련된 비즈니스로직이 존재하는 객체
public class Registrar {
    private final LectureRepository lectureRepo;
    private final CourseRepository courseRepo;
    private final Set<RegisteringPeriod> periodSet;

    public Registrar(LectureRepository lectureRepo, CourseRepository courseRepo,
                        Set<RegisteringPeriod> periodSet){
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
        this.periodSet = periodSet;
    }

    //수강신청 기능 (수강신청 제약사항 확인후 수강객체 생성하여 반환)
    public Registering register(Lecture lecture, Student student) throws  IllegalStateException, IllegalArgumentException{
        Course course = lecture.getCourse();

        //수강신청하는 학생의 중복된 수강 확인
        for(Registering registering : student.getMyRegisterings()){
            Lecture stdLecture = lectureRepo.findByID(registering.getLectureID());

            if(stdLecture.isEqualCourse(lecture)){
                throw new IllegalArgumentException("중복된 수강입니다.");
            }
        }

        //수강신청 기간확인
        if(!isValidPeriodAbout(student, course)){
            throw new IllegalStateException("해당학년 수강신청 기간이 아닙니다.");
        }

        //최대수강인원 확인
        if(!lecture.validLimitNum()){
            throw new IllegalStateException("수강인원이 초과 되었습니다");
        }

        //수강시간 겹치는지 확인
        if(student.isDuplicatedTime(lecture.getLectureTimes())){
            throw new IllegalArgumentException("같은시간에 강의를 수강중입니다.");
        }

        //학생 최대 수강신청가능 학점 확인
        if(!student.isValidCredit(course.getCredit())){
            throw new IllegalArgumentException("수강할수 있는 학점을 초과했습니다.");
        }

        //수강객체 생성
        Registering newReg = Registering.builder()
                                .lectureID(lecture.getID())
                                .studentCode(student.getStudentCode())
                                .registeringTime(LocalDateTime.now().toString())
                                .build();

        //개설교과목과 학생에게 수강과 관련된 변경 사항 저장
        lecture.register(newReg);
        student.register(newReg, lecture.getLectureTimes(), course.getCredit());

        return newReg;
    }

    //수강취소 기능(제약사항)
    public void cancel(Registering registering, Student student, Lecture lecture){
        //학생이 개설교과목 수강하는지 확인
        if(!student.hasLecture(lecture.getID())){
            throw new IllegalArgumentException("수강하지 않는 강의 입니다.");
        }
        Course course = lecture.getCourse();

        //학생과 개설교과목 각각 수강 삭제
        student.cancel(registering, lecture.getLectureTimes(), course.getCredit());
        lecture.cancel(registering);
    }

    //특정학생이 수강가능한지 확인
    public boolean isValidPeriodAbout(Student std, Course course){
        for(RegisteringPeriod period : periodSet){
            if(period.isSatisfiedBy(std, course)){
                return true;
            }
        }

        return false;
    }
}
