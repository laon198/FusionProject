package domain.model;

import domain.generic.LectureTime;

import java.time.LocalDate;
import java.util.*;

public class Professor extends Member{
    private Set<LectureTime> timeTable;
    private String professorCode;


    public static class Builder{
        private long id;
        private String name;
        private String department;
        private String birthDate;
        private String professorCode;
        private Set<LectureTime> timeTable = new HashSet<>();

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder name(String value){
            name = value;
            return this;
        }

        public Builder department(String value){
            department = value;
            return this;
        }

        public Builder birthDate(String value){
            birthDate = value;
            return this;
        }

        public Builder professorCode(String value){
            professorCode = value;
            return this;
        }

        public Builder timeTable(LectureTime... lectureTimes){
            timeTable = new HashSet(Arrays.asList(lectureTimes));
            return this;
        }

        public Professor build(){
            return new Professor(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    public Professor(Builder builder){
        super(builder.id, builder.name, builder.department, builder.birthDate);
        timeTable = builder.timeTable;
        professorCode = builder.professorCode;
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
