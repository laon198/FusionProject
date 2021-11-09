package domain.model;

import domain.generic.LectureTime;
import domain.generic.Period;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Lecture {
    private long id;
    private long courseID;
    private String lectureCode;
    private String lecturerID;
    private int limit;
    private String professor;
    private Set<LectureTime> lectureTimes;
    private Set<Registering> myRegisterings;
    private LecturePlanner planner;

    //TODO : set을 직접 받는 것 별로 좋지 않은듯
    public Lecture(){}
    public Lecture(long lectureID, String professorID, int limitPersonNum, long courseID, String lectureCode,
                   Set<LectureTime> lectureTimes, Set<Registering> registerings){ //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
        this.lectureCode = lectureCode;
        myRegisterings = registerings;
    }

    //TODO : LectureManageService 때문에 만들어 놓은 임시 생성자. 나중에 지우자.
    public Lecture(long lectureID, String professorID, int limitPersonNum, long courseID,
                   Set<LectureTime> lectureTimes){ //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        myRegisterings = new HashSet<>(); //TODO
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
    }

    public long getID(){return id;}
    public long getCourseID(){ return courseID;}
    public Set<LectureTime> getLectureTimes(){ return lectureTimes;}

    public void setLimit(int limit){
        if(limit<0){
            throw new IllegalArgumentException("최소 수강인원은 0명 부터입니다.");
        }
        this.limit = limit;
    }

    public void register(Registering registering){
        myRegisterings.add(registering);
    }

    public boolean validLimitNum(){
        if(myRegisterings.size()>limit){
            return false;
        }
        return true;
    }


    public void cancel(Registering registering) {
        myRegisterings.remove(registering);
    }

    public void writePlanner(String itemName, String content, String writerID) {
        if(writerID!=lecturerID){
            throw new IllegalStateException("담당 교과목이 아닙니다.");
        }

        planner.setItem(itemName, content);
    }

    public void setPlannerWritingPeriod(Period period){
        planner.setWritingPeriod(period);
    }

    public boolean isEqualCourse(Lecture lecture) {
        return lecture.courseID==courseID;
    }

    public boolean hasLectureTime(LectureTime lTime){
        return lectureTimes.contains(lTime);
    }

    public void removeTime(LectureTime time) { lectureTimes.remove(time); }

    public void addTime(LectureTime time) { lectureTimes.add(time); }

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
