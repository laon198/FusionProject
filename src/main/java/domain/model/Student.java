package domain.model;

import domain.generic.LectureTime;

import java.util.*;

public class Student extends Member{
    private Year year;
    private int maxCredit;
    private int credit;
    private String studentCode;
    private final List<Long> registeredLectureIDs;
    private final Set<LectureTime> timeTable;

    public enum Year {
        FRESHMAN(1), SOPHOMORE(2), JUNIOR(3), SENIOR(4);
        private final int year;
        Year(int year){
            this.year = year;
        }
    }

    public static class Builder{
        private Long id;
        private int credit;
        private int maxCredit=21;
        private Year year;
        private String name;
        private String department;
        private String studentCode;
        private String birthDate;
        private List<Long> registeredLectureIDs = new ArrayList<Long>();
        private Set<LectureTime> timeTable = new HashSet<LectureTime>();

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

        public Builder studentCode(String value){
            studentCode = value;
            return this;
        }

        public Builder year(int value){
            if(value==1){
                year = Year.FRESHMAN;
            }else if(value==2){
                year = Year.SOPHOMORE;
            }else if(value==3){
                year = Year.JUNIOR;
            }else if(value==4){
                year = Year.SENIOR;
            }else{
                throw new IllegalArgumentException("1~4학년만 존재합니다.");
            }
            return this;
        }

        public Builder credit(int value){
            credit = value;
            return this;
        }

        public Builder maxCredit(int value){
            maxCredit = value;
            return this;
        }

        public Builder registeredLectureIDs(List<Long> value){
            registeredLectureIDs = new ArrayList<>(value);
            return this;
        }

        public Builder timeTable(Set<LectureTime> value){
            timeTable = new HashSet(value);
            return this;
        }

        public Student build(){
            return new Student(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    private Student(Builder builder){
        super(builder.id, builder.name,
                builder.department, builder.birthDate);
        year = builder.year;
        registeredLectureIDs = builder.registeredLectureIDs;
        timeTable = builder.timeTable;
        credit = builder.credit;
        maxCredit = builder.maxCredit;
        studentCode = builder.studentCode;
    }

    public long getID(){return id;}
    public Long[] getRegisteredLectureIDs(){return (Long[])registeredLectureIDs.toArray();}
    public Year getYear(){return year;}

    public void register(long lectureID,
                            Set<LectureTime> lectureTimes, int lectureCredit){
        credit += lectureCredit;
        registeredLectureIDs.add(lectureID);
        timeTable.addAll(lectureTimes);
    }

    public void cancel(long lectureID,
                       Set<LectureTime> lectureTimes, int lectureCredit){
        credit -= lectureCredit;
        registeredLectureIDs.remove(lectureID);
        timeTable.removeAll(lectureTimes);
    }

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

    public boolean isValidCredit(int lectureCredit) {
        if(credit+lectureCredit>=maxCredit){
            return false;
        }

        return true;
    }

    public boolean hasLecture(long lectureID) {
        for(long myLectureID : registeredLectureIDs){
            if(myLectureID==lectureID){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
