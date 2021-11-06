package domain.model;

import java.util.Objects;

public class Course {
    private long id;
    private String courseCode;
    private String department;
    private int credit;

    public Course(long id, int credit){
        this.id = id;
        this.credit = credit;
    }

    public long getId() {
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
