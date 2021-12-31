package domain.model;

import java.util.*;

public class Professor extends Member{
    private Set<LectureTime> timeTable;
    private String professorCode;
    private String telePhone;

    public static class Builder extends Member.Builder<Builder>{
        private String professorCode;
        private String telePhone;
        private Set<LectureTime> timeTable = new HashSet<>();

        private Builder(String name, String department, String birthDate,
                            String professorCode){
            super(name, department, birthDate);
            this.professorCode = professorCode;
        }

        public Builder telePhone(String value){
            telePhone = value;
            return this;
        }

        public Builder timeTable(Set<LectureTime> value){
            timeTable = new HashSet(value);
            return this;
        }

        public Professor build(){
            return new Professor(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }//end of builder class

    public static Builder builder(String name, String department, String birthDate,
                                  String professorCode){
        return new Builder(name, department, birthDate, professorCode);
    }

    public Professor(Builder builder){
        super(builder);
        timeTable = builder.timeTable;
        professorCode = builder.professorCode;
        telePhone = builder.telePhone;
    }


    //시간표 추가 : 담당교과목이 추가되었을때
    public void addTimeTable(Set<LectureTime> lectureTimes){
        if(isDuplicatedLectureTime(lectureTimes)){
            throw new IllegalArgumentException("같은시간에 강의가 존재합니다.");
        }

        timeTable.addAll(lectureTimes);
    }

    //나의 시간표내의 시간과 겹치는 시간이 있는지 확인
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

    public void setTelePhone(String value){
        telePhone = value;
    }
    public void setName(String value){name = value;}

    public String getProfessorCode(){return professorCode;}
}
