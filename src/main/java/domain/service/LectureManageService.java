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

    public void addLecture(LectureID lectureID, CourseID courseID, ProfessorID professorID,
                           int limit, Set<LectureTime> lectureTimes){
        //TODO : lectureID확인 로직 필요
        courseRepo.findByID(courseID); //TODO : 존재하지않을때 예외 로직필요
        Professor p = professorRepo.findByID(professorID); //TODO : 존재하지않을때 예외 로직필요

        //TODO : 더 좋은방법?
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

    public void removeLecture(){
    }

    public void updateLecture(){
    }
}
