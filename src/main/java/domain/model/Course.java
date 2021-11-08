package domain.model;

import java.util.Objects;

public class Course {
    private long id;
    private String courseCode;
    private String courseName;
    private String department;
    private int targetYear;
    private int credit;

    public Course(){}

    public Course(long id, String courseCode, String courseName, String department, int targetYear, int credit) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
        this.targetYear = targetYear;
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
