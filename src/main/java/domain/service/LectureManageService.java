package domain.service;

import domain.generic.LectureTime;
import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.ProfessorRepository;

import java.util.Set;

public class LectureManageService {
    private CourseRepository courseRepo;
    private ProfessorRepository professorRepo;
    private LectureRepository lectureRepo;

    public LectureManageService(CourseRepository courseRepo,
                                    ProfessorRepository professorRepo,
                                        LectureRepository lectureRepo){
        this.courseRepo = courseRepo;
        this.professorRepo = professorRepo;
        this.lectureRepo = lectureRepo;
    }

<<<<<<< HEAD
    public void addLecture(long lectureID, long courseID, long professorID,
=======
    public void addLecture(LectureID lectureID, CourseID courseID, ProfessorID professorID,
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
                           int limit, Set<LectureTime> lectureTimes){
        //TODO : lectureID확인 로직 필요
        courseRepo.findByID(courseID); //TODO : 존재하지않을때 예외 로직필요
        Professor p = professorRepo.findByID(professorID); //TODO : 존재하지않을때 예외 로직필요

        //TODO : 더 좋은방법?
<<<<<<< HEAD
        //TODO : 강의를 다가져오기에는 부하가 너무 크지않나?
=======
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        for(Lecture existingLecture : lectureRepo.findAll()){
            for(LectureTime existingTime : existingLecture.getLectureTimes()){
                for(LectureTime newTime : lectureTimes){
                    if(existingTime.isSameRoom(newTime) &&
                        existingTime.isOverlappedTime(newTime)){
                        throw new IllegalArgumentException("이미 존재하는 시간입니다.");
                    }
                }
            }
        }

        //TODO : 다른방식으로 생성?
        Lecture newLecture = new Lecture(
                lectureID,
                professorID,
                limit,
                courseID,
                lectureTimes
        );

        p.addTimeTable(lectureTimes);

        lectureRepo.save(newLecture);
    }

<<<<<<< HEAD
    //TODO : 굳이 여기서 할필요가 있나? 디비에서 처리가능한거 아닌가?
    //TODO : cascade하는 로직은 디비에서?
    public void removeLecture(long lectureID){
        lectureRepo.remove(lectureID);
    }

    //TODO : 교수와 학생의 강의시간 의존 문제
    public void updateAboutRoom(long lectureID,
                                    LectureTime targetLectureTime, String room){
        Lecture targetLecture = lectureRepo.findByID(lectureID);

        if(!targetLecture.hasLectureTime(targetLectureTime)){
            throw new IllegalArgumentException("시간과 강의실이 잘못되었습니다.");
        }

        LectureTime updatedTime = targetLectureTime.setRoom(room);

        for(Lecture existingLecture : lectureRepo.findAll()){
            for(LectureTime existingTime : existingLecture.getLectureTimes()){
                if(existingTime.isSameRoom(updatedTime) &&
                        existingTime.isOverlappedTime(updatedTime)){
                    throw new IllegalArgumentException("이미 해당시간에 강의실이 사용중 입니다.");
                }
            }
        }

        targetLecture.removeTime(targetLectureTime);
        targetLecture.addTime(updatedTime);

        lectureRepo.save(targetLecture);
    }

    public void updateAboutMaxStd(long lectureID, int max){
        Lecture targetLecture = lectureRepo.findByID(lectureID);
        targetLecture.setLimit(max);
        lectureRepo.save(targetLecture);
    }
}
=======
    public void removeLecture(){
    }

    public void updateLecture(){
    }
}
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
