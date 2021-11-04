package domain.model;

import domain.generic.LectureTime;
import domain.generic.Period;

import java.util.*;

public class Lecture {
    private LectureID id;
    private int limit;
    private CourseID courseID;
    private ProfessorID lecturerID;
    private Set<LectureTime> lectureTimes;
    private List<StudentID> registeredStudentIDs;
    private LecturePlanner planner;

    public Lecture(LectureID lectureID, ProfessorID professorID, int limitPersonNum, CourseID courseID,
                   Set<LectureTime> lectureTimes){ //TODO : 강의시간 더나은 방법으로
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        registeredStudentIDs = new ArrayList<>(limitPersonNum);
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
    }

    public LectureID getID(){return id;}
    public CourseID getCourseID(){ return courseID;}
    public Set<LectureTime> getLectureTimes(){ return lectureTimes;}

    public void register(StudentID stdID){
        registeredStudentIDs.add(stdID);
    }

    public boolean validLimitNum(){
        if(registeredStudentIDs.size()>limit){
            return false;
        }
        return true;
    }

    public void cancel(StudentID stdID) {
        registeredStudentIDs.remove(stdID);
    }

    public void writePlanner(String itemName, String content, ProfessorID writerID) {
        if(!writerID.equals(lecturerID)){
            throw new IllegalStateException("담당 교과목이 아닙니다.");
        }

        planner.setItem(itemName, content);
    }

    public void setPlannerWritingPeriod(Period period){
        planner.setWritingPeriod(period);
    }

    public boolean isEqualCourse(Lecture lecture) {
        return lecture.courseID.equals(courseID);
    }

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
