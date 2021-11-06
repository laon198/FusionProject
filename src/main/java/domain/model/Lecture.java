package domain.model;

import domain.generic.LectureTime;
import domain.generic.Period;

import java.util.*;

public class Lecture {
<<<<<<< HEAD
    private long id;
    private long courseID;
    private long lecturerID;
    private int limit;
    private Set<LectureTime> lectureTimes;
    private List<Long> registeredStudentIDs;
    private LecturePlanner planner;

    //TODO : set을 직접 받는 것 별로 좋지 않은듯
    public Lecture(long lectureID, long professorID, int limitPersonNum, long courseID,
=======
    private LectureID id;
    private int limit;
    private CourseID courseID;
    private ProfessorID lecturerID;
    private Set<LectureTime> lectureTimes;
    private List<StudentID> registeredStudentIDs;
    private LecturePlanner planner;

    public Lecture(LectureID lectureID, ProfessorID professorID, int limitPersonNum, CourseID courseID,
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
                   Set<LectureTime> lectureTimes){ //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        registeredStudentIDs = new ArrayList<>(limitPersonNum);
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
    }

<<<<<<< HEAD
    public long getID(){return id;}
    public long getCourseID(){ return courseID;}
    public Set<LectureTime> getLectureTimes(){ return lectureTimes;}

    public void setLimit(int limit){
        if(limit<0){
            throw new IllegalArgumentException("최소 수강인원은 0명 부터입니다.");
        }
        this.limit = limit;
    }

    public void register(long stdID){
=======
    public LectureID getID(){return id;}
    public CourseID getCourseID(){ return courseID;}
    public Set<LectureTime> getLectureTimes(){ return lectureTimes;}

    public void register(StudentID stdID){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        registeredStudentIDs.add(stdID);
    }

    public boolean validLimitNum(){
        if(registeredStudentIDs.size()>limit){
            return false;
        }
        return true;
    }

<<<<<<< HEAD
    public void cancel(long stdID) {
        registeredStudentIDs.remove(stdID);
    }

    public void writePlanner(String itemName, String content, long writerID) {
        if(writerID!=lecturerID){
=======
    public void cancel(StudentID stdID) {
        registeredStudentIDs.remove(stdID);
    }

    public void writePlanner(String itemName, String content, ProfessorID writerID) {
        if(!writerID.equals(lecturerID)){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
            throw new IllegalStateException("담당 교과목이 아닙니다.");
        }

        planner.setItem(itemName, content);
    }

    public void setPlannerWritingPeriod(Period period){
        planner.setWritingPeriod(period);
    }

    public boolean isEqualCourse(Lecture lecture) {
<<<<<<< HEAD
        return lecture.courseID==courseID;
    }

    public boolean hasLectureTime(LectureTime lTime){
        return lectureTimes.contains(lTime);
    }

    public void removeTime(LectureTime time) { lectureTimes.remove(time); }

    public void addTime(LectureTime time) { lectureTimes.add(time); }

=======
        return lecture.courseID.equals(courseID);
    }

>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
