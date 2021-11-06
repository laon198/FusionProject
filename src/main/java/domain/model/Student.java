package domain.model;

import domain.generic.LectureTime;

import java.util.*;

public class Student{
<<<<<<< HEAD
    private long id;
    private int maxCredit = 21;
    private int credit;
    private List<Long> registeredLectureIDs;
=======
    private StudentID id;
    private int maxCredit = 21;
    private int credit;
    private List<LectureID> registeredLectureIDs;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    private Set<LectureTime> timeTable;
    private Year year;

    public enum Year {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    }

<<<<<<< HEAD
    public Student(long stdID, Year year){
=======
    public Student(StudentID stdID, Year year){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        this.id = stdID;
        this.year = year;
        registeredLectureIDs = new ArrayList<>();
        timeTable = new HashSet<>();
    }

<<<<<<< HEAD
    public long getID(){return id;}
    public List<Long> getRegisteredLectureIDs(){return registeredLectureIDs;}
    public Year getYear(){return year;}

    public void register(long lectureID,
=======
    public StudentID getID(){return id;}
    public List<LectureID> getRegisteredLectureIDs(){return registeredLectureIDs;}
    public Year getYear(){return year;}

    public void register(LectureID lectureID,
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
                            Set<LectureTime> lectureTimes, int lectureCredit){
        credit += lectureCredit;
        registeredLectureIDs.add(lectureID);
        timeTable.addAll(lectureTimes);
    }

<<<<<<< HEAD
    public void cancel(long lectureID,
=======
    public void cancel(LectureID lectureID,
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
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

<<<<<<< HEAD
    public boolean hasLecture(long lectureID) {
        for(long myLectureID : registeredLectureIDs){
            if(myLectureID==lectureID){
=======
    public boolean hasLecture(LectureID lectureID) {
        for(LectureID myLectureID : registeredLectureIDs){
            if(myLectureID.equals(lectureID)){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
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
