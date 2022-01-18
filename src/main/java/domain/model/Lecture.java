package domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Lecture {
    private long id;
    private long courseID;
    private String lectureCode;
    private long professorID;
    private int limit;
    private Set<LectureTime> lectureTimes;
    private Set<Registering> myRegisterings;
    private LecturePlanner planner;

    public static class Builder {
        private long id;
        private long courseID;
        private String lectureCode;
        private long professorID;
        private int limit;
        private Set<LectureTime> lectureTimes = new HashSet<>();
        private Set<Registering> myRegisterings = new HashSet<>();
        private LecturePlanner planner = new LecturePlanner();


        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder courseID(long value) {
            courseID = value;
            return this;
        }

        public Builder lectureCode(String lectureCode) {
            this.lectureCode = lectureCode;
            return this;
        }

        public Builder professorID(long value) {
            professorID = value;
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
        courseID = builder.courseID;
        lectureCode = builder.lectureCode;
        professorID = builder.professorID;
        limit = builder.limit;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

    public Lecture() {}

    public long getID() {
        return id;
    }

    public long getCourseID() {
        return courseID;
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

    public  void setProfessorID(long id){
        professorID = id;
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
        return courseID==lecture.courseID;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", courseID=" + courseID +
                ", lectureCode='" + lectureCode + '\'' +
                ", professorID=" + professorID +
                ", limit=" + limit +
                ", lectureTimes=" + lectureTimes +
                ", myRegisterings=" + myRegisterings +
                ", planner=" + planner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Lecture)) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
