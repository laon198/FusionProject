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

    //TODO : 굳이 여기서 할필요가 있나? 디비에서 처리가능한거 아닌가?
    //TODO : cascade하는 로직은 디비에서?
    public void removeLecture(LectureID lectureID){
        lectureRepo.remove(lectureID);
    }

    public void updateAboutRoom(LectureID lectureID, String room){
        //강의 시간표 여러개중에 어떤거 가져올꺼지?
        //전체 개설 교과목 가져온다.
        //전체 개설 교과목과 겹치는 시(공)간 있는지 확인
        //있으면 예외, 없으면 변경
    }
}