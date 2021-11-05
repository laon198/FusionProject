package domain.repository;

import domain.model.Course;
import domain.model.CourseID;
import domain.model.Lecture;

public interface CourseRepository {
    public Course findByID(CourseID id);
    public void save(Course Course);
}
