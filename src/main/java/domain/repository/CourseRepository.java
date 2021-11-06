package domain.repository;

import domain.model.Course;
<<<<<<< HEAD

public interface CourseRepository {
    public Course findByID(long id);
=======
import domain.model.CourseID;
import domain.model.Lecture;

public interface CourseRepository {
    public Course findByID(CourseID id);
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    public void save(Course Course);
}
