package domain.model;

import domain.generic.LectureTime;

import java.util.HashSet;
import java.util.Set;

public class Professor extends Member{
    private Set<LectureTime> timeTable;

    public Professor(long id){
        super(id, "a", "b"); //TODO
        timeTable = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void addTimeTable(Set<LectureTime> lectureTimes){
        if(isDuplicatedLectureTime(lectureTimes)){
            throw new IllegalArgumentException("같은시간에 강의가 존재합니다.");
        }

        timeTable.addAll(lectureTimes);
    }

    //TODO : lectureTimes를 하나의 객체로?
    private boolean isDuplicatedLectureTime(Set<LectureTime> lectureTimes){
        for(LectureTime myTime : timeTable){
            for(LectureTime lectureTime : lectureTimes){
                if(myTime.isOverlappedTime(lectureTime)){
                    return true;
                }
            }
        }
        return false;
    }
}
