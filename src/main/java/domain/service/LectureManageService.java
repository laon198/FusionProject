package domain.service;

import domain.model.LectureTime;
import domain.model.*;
import domain.repository.LectureRepository;

import java.util.Set;

//개설교과목과 관련된 제약사항 구현한 객체
public class LectureManageService {
    private LectureRepository lectureRepo;

    public LectureManageService(LectureRepository lectureRepo){
        this.lectureRepo = lectureRepo;
    }

    //생성과 관련된 제약사항 수행
    public Lecture create(Course course, String lectureCode, Professor professor,
            int limit, Set<LectureTime> lectureTimes){

        //현재 존재하는 시간이면, 강의 생성불가
        if(isExistingTimes(lectureTimes)){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        //강의 객체 생성해서 반환
        return Lecture.builder()
                .courseID(course.getId())
                .lectureCode(lectureCode)
                .professorID(professor.getID())
                .limit(limit)
                .lectureTimes(lectureTimes)
                .build();
    }


    //수정과 관련된 제약사항
    public Lecture update(Lecture lecture){
        //현재 존재하는 시간이면, 변경 불가
        if(isExistingTimes(lecture.getLectureTimes())){
            throw new IllegalArgumentException("유효하지않은 시간입니다.");
        }

        return lecture;
    }

    //현재 존재하는 강의시간인지 확인
    private boolean isExistingTimes(Set<LectureTime> times){
        try{
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
        }catch(IllegalArgumentException e){
            return false;
        }
        return false;
    }
}
