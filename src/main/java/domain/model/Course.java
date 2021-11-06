package domain.model;

import java.util.Objects;

public class Course {
<<<<<<< HEAD
    private long id;
=======
    private CourseID id;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    private String courseCode;
    private String department;
    private int credit;

<<<<<<< HEAD
    public Course(long id, int credit){
=======
    public Course(CourseID id, int credit){
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        this.id = id;
        this.credit = credit;
    }

<<<<<<< HEAD
    public long getId() {
        return id;
    }
=======
    public CourseID getId() {
        return id;
    }

>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
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
