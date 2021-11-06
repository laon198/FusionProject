package domain.service;

<<<<<<< HEAD
import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.StudentRepository;
=======
import domain.model.Course;
import domain.model.Lecture;
import domain.model.LectureID;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.model.Student;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd

import java.util.HashSet;
import java.util.Set;

public class RegisterService {
    private LectureRepository lectureRepo;
    private CourseRepository courseRepo;
<<<<<<< HEAD
    private StudentRepository studentRepo;
    private Set<RegisteringPeriod> periodSet;

    public RegisterService(LectureRepository lectureRepo, CourseRepository courseRepo,
                            StudentRepository studentRepo){
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
=======
    private Set<RegisteringPeriod> periodSet;

    public RegisterService(LectureRepository lectureRepo, CourseRepository courseRepo){
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        periodSet = new HashSet<>();
    }

    //TODO : 바로 객체 받을지 아이디로 받을지 생각
<<<<<<< HEAD
    public void register(long lectureID, long studentID){
        Lecture lecture = lectureRepo.findByID(lectureID);
        Student student = studentRepo.findByID(studentID);
=======
    public void register(Lecture lecture, Student student){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        Course course = courseRepo.findByID(lecture.getCourseID());

        if(!isValidPeriodAbout(student)){
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
<<<<<<< HEAD
        for(long id : student.getRegisteredLectureIDs()){
=======
        for(LectureID id : student.getRegisteredLectureIDs()){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
            Lecture stdLecture = lectureRepo.findByID(id);

            if(stdLecture.isEqualCourse(lecture)){
                throw new IllegalArgumentException("중복된 수강입니다.");
            }
        }

        lecture.register(student.getID());
        student.register(lecture.getID(), lecture.getLectureTimes(), course.getCredit());
<<<<<<< HEAD

        lectureRepo.save(lecture);
        studentRepo.save(student);
    }

    public void cancel(long lectureID, long studentID){
        Lecture lecture = lectureRepo.findByID(lectureID);
        Student student = studentRepo.findByID(studentID);

=======
    }

    public void cancel(Lecture lecture, Student student){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        if(!student.hasLecture(lecture.getID())){
            throw new IllegalArgumentException("수강하지 않는 강의 입니다.");
        }
        Course course = courseRepo.findByID(lecture.getCourseID());

        student.cancel(lecture.getID(), lecture.getLectureTimes(), course.getCredit());
        lecture.cancel(student.getID());
<<<<<<< HEAD

        lectureRepo.save(lecture);
        studentRepo.save(student);
=======
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    }

    public boolean isValidPeriodAbout(Student std){
        for(RegisteringPeriod period : periodSet){
            if(period.isSatisfiedBy(std)){
                return true;
            }
        }

        return false;
    }

    //TODO : 겹치는 시간 처리? 좀더 잘처리
    public void addRegisteringPeriod(RegisteringPeriod period){
        periodSet.add(period);
    }
<<<<<<< HEAD

    public void removeRegisteringPeriod(RegisteringPeriod rPeriod) {
        periodSet.remove(rPeriod);
    }
=======
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
}
