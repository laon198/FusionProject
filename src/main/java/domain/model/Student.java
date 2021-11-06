package domain.model;

import domain.generic.LectureTime;

import java.util.*;

public class Student{
    private long id;
    private int maxCredit = 21;
    private int credit;
    private List<Long> registeredLectureIDs;
    private Set<LectureTime> timeTable;
    private Year year;

    public enum Year {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    }

    public Student(long stdID, Year year){
        this.id = stdID;
        this.year = year;
        registeredLectureIDs = new ArrayList<>();
        timeTable = new HashSet<>();
    }

    public long getID(){return id;}
    public List<Long> getRegisteredLectureIDs(){return registeredLectureIDs;}
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
