package domain.service;

import domain.model.LectureTime;
import domain.model.*;
import domain.repository.LectureRepository;

import java.util.Set;

public class LectureManageService {
    private LectureRepository lectureRepo;

    public LectureManageService(LectureRepository lectureRepo){
        this.lectureRepo = lectureRepo;
    }

    public Lecture create( long courseID, String lectureCode, String professorCode,
            int limit, Set<LectureTime> lectureTimes){
        //TODO : lectureID확인 로직 필요
//        Professor p = professorRepo.findByID(professorID); //TODO : 존재하지않을때 예외 로직필요

        //TODO : 더 좋은방법?
        //TODO : 강의를 다가져오기에는 부하가 너무 크지않나?
        if(isExistingTimes(lectureTimes)){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        //TODO : 다른방식으로 생성?
        return Lecture.builder()
                .courseID(courseID)
                .lectureCode(lectureCode)
                .lecturerID(professorCode)
                .limit(limit)
                .lectureTimes(lectureTimes)
                .build();
    }


    //TODO : 교수와 학생의 강의시간 의존 문제
    public Lecture update(Lecture lecture){
        if(isExistingTimes(lecture.getLectureTimes())){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        return lecture;
    }

    private boolean isExistingTimes(Set<LectureTime> times){
        for(Lecture existingLecture : lectureRepo.findAll()){
            for(LectureTime existingTime : existingLecture.getLectureTimes()){
                for(LectureTime newTime : times){
                    if(existingTime.isSameRoom(newTime) &&
                            existingTime.isOverlappedTime(newTime)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
