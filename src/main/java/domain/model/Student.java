package domain.model;

import java.util.*;

public class Student extends Member{
    private Year year;
    private int maxCredit;
    private int credit;
    private String studentCode;
    private final Set<Registering> myRegisterings;
    private final Set<LectureTime> timeTable;

    public static class Builder extends Member.Builder<Builder>{
        private Year year;
        private String studentCode;
        private int credit=0;
        private int maxCredit=21;
        private Set<Registering> myRegisterings = new HashSet<>();
        private Set<LectureTime> timeTable = new HashSet<>();

        private Builder(String name, String department, String birthDate,
                                String studentCode, Year year){
            super(name, department, birthDate);
            this.studentCode = studentCode;
            this.year = year;
        }

        public Builder credit(int value){
            credit = value;
            return this;
        }

        public Builder maxCredit(int value){
            maxCredit = value;
            return this;
        }

        public Builder myRegisterings(Set<Registering> value){
            myRegisterings = value;
            return this;
        }

        public Builder timeTable(Set<LectureTime> value){
            timeTable = new HashSet(value);
            return this;
        }

        @Override
        public Student build(){
            return new Student(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }//end of builder class

    public static Builder builder(String name, String department, String birthDate,
                                  String studentCode, Year year){
        return new Builder(name, department, birthDate, studentCode, year);
    }

    private Student(Builder builder){
        super(builder);
        myRegisterings = builder.myRegisterings;
        timeTable = builder.timeTable;
        credit = builder.credit;
        maxCredit = builder.maxCredit;
    }

    @Override
    public String getCode() {
        return studentCode;
    }

    public Set<Registering> getMyRegisterings(){
        return new HashSet<>(myRegisterings);
    }

    public Year getYear(){return year;}

    public String getStudentCode() {
        return studentCode;
    }

    public void setName(String value){
        name = value;
    }

    //??????????????? ?????? ???????????????, ?????????, ???????????? ??????
    public void register(Registering registering,
                            Set<LectureTime> lectureTimes, int lectureCredit){
        credit += lectureCredit;
        myRegisterings.add(registering);
        timeTable.addAll(lectureTimes);
    }

    //??????????????? ?????? ???????????????, ?????????, ???????????? ??????
    public void cancel(Registering registering,
                       Set<LectureTime> lectureTimes, int lectureCredit){
        credit -= lectureCredit;
        myRegisterings.remove(registering);
        timeTable.removeAll(lectureTimes);
    }

    //?????? ???????????? ????????? ???????????? ??????
    public boolean isDuplicatedTime(Set<LectureTime> lectureTimes){
        for(LectureTime myTime : timeTable){
            for(LectureTime lectureTime : lectureTimes){
                if(myTime.isOverlappedTime(lectureTime)){
                    return true;
                }
            }
        }
        return false;
    }

    //??????????????? ?????? ??????????????? ??????????????? ??????
    public boolean isValidCredit(int lectureCredit) {
        if(credit+lectureCredit>=maxCredit){
            return false;
        }

        return true;
    }

    //?????? ?????????????????? ????????????????????? ??????
    public boolean hasLecture(long lectureID) {
        for(Registering registering : myRegisterings){
            if(registering.getLectureID()==lectureID){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", year=" + year +
                ", maxCredit=" + maxCredit +
                ", credit=" + credit +
                ", studentCode='" + studentCode + '\'' +
                ", myRegisterings=" + myRegisterings +
                ", timeTable=" + timeTable +
                '}';
    }
}
