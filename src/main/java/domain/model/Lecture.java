package domain.model;

import infra.dto.ProfessorDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Lecture {
    private long id;
    private Course course;
    private String lectureCode;
    private Professor professor;
    private int limit;
    private Set<LectureTime> lectureTimes;
    private Set<Registering> myRegisterings;
    private LecturePlanner planner;

    public static class Builder {
        private long id;
        private Course course;
        private String lectureCode;
        private Professor professor;
        private int limit;
        private Set<LectureTime> lectureTimes = new HashSet<>();
        private Set<Registering> myRegisterings = new HashSet<>();
        private LecturePlanner planner = new LecturePlanner();


        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder course(Course course) {
            this.course = course;
            return this;
        }

        public Builder lectureCode(String lectureCode) {
            this.lectureCode = lectureCode;
            return this;
        }

        public Builder professor(Professor value) {
            professor = value;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder lectureTimes(Set<LectureTime> lectureTimes) {
            this.lectureTimes = lectureTimes;
            return this;
        }

        public Builder registerings(Set<Registering> myRegisterings) {
            this.myRegisterings = myRegisterings;
            return this;
        }

        public Builder planner(LecturePlanner planner) {
            this.planner = planner;
            return this;
        }

        public Lecture build() {
            return new Lecture(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Lecture(Builder builder) {
        id = builder.id;
        course = builder.course;
        lectureCode = builder.lectureCode;
        professor = builder.professor;
        limit = builder.limit;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

    public Lecture() {}

//    public Lecture(long lectureID, String professorID, int limitPersonNum, Course course, String lectureCode,
//                   Set<LectureTime> lectureTimes, Set<Registering> registerings) {
//        id = lectureID;
//        lecturerID = professorID;
//        limit = limitPersonNum;
//        this.lectureTimes = lectureTimes;
//        this.course = course;
//        planner = new LecturePlanner();
//        this.lectureCode = lectureCode;
//        myRegisterings = registerings;
//    }

    public long getID() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Set<LectureTime> getLectureTimes() {
        return lectureTimes;
    }

    public void setLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("최소 수강인원은 0명 부터입니다.");
        }
        this.limit = limit;
    }

    public void setLectureCode(String value){
        lectureCode = value;
    }

    public  void setProfessor(Professor professor){
        this.professor = professor;
    }

    public void setLectureTimes(Set<LectureTime> lectureTimes) {
        this.lectureTimes = lectureTimes;
    }

    public void register(Registering registering) {
        myRegisterings.add(registering);
    }

    public void writePlanner(String itemName, String content) throws IllegalArgumentException{

        planner.writeItem(itemName, content);
    }

    public boolean validLimitNum() {
        if (myRegisterings.size() >= limit) {
            return false;
        }
        return true;
    }

    public void cancel(Registering registering) {
        myRegisterings.remove(registering);
    }

    public boolean isEqualCourse(Lecture lecture) {
        return course.equals(lecture.course);
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

    //TODO : 테스트용
    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", lectureCode='" + lectureCode + '\'' +
                ", limit=" + limit +
                ", lectureTimes=" + lectureTimes +
                ", myRegisterings=" + myRegisterings +
                ", planner=" + planner +
                '}';
    }
}
