package domain.model;

import java.util.Objects;

public class Course {
    private CourseID id;
    private String courseCode;
    private String department;
    private int credit;

    public Course(CourseID id, int credit){
        this.id = id;
        this.credit = credit;
    }

    public CourseID getId() {
        return id;
    }

    public int getCredit() {
        return credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
