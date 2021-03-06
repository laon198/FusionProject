package domain.model;

import java.util.Objects;

public class Course {
    private long id;
    private String courseCode;
    private String courseName;
    private String department;
    private Year targetYear;
    private int credit;

    public Year getYear() {
        return targetYear;
    }

    public static class Builder {
        private long id = -1;
        private String courseCode;
        private String courseName;
        private String department;
        private Year targetYear;
        private int credit;

        private Builder(String courseCode, String courseName, String department,
                            Year targetYear, int credit){
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.department = department;
            this.targetYear = targetYear;
            this.credit = credit;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

    public static Builder builder(String courseCode, String courseName, String department,
                                  Year targetYear, int credit) {
        return new Builder(courseCode, courseName, department, targetYear, credit);
    }

    public Course() {}

    private Course(Builder builder) {
        id = builder.id;
        courseCode = builder.courseCode;
        courseName = builder.courseName;
        department = builder.department;
        targetYear = builder.targetYear;
        credit = builder.credit;
    }

    public Course(long id, String courseCode, String courseName, String department, Year targetYear, int credit) {
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

    public void setCourseCode(String value) {
        courseCode = value;
    }

    public void setDepartment(String value) {
        department = value;
    }

    public void setTargetYear(Year value) {
        targetYear = value;
    }

    public void setCourseName(String value) {
        courseName = value;
    }

    public void setCredit(int value) {
        credit = value;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", department='" + department + '\'' +
                ", targetYear=" + targetYear +
                ", credit=" + credit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
