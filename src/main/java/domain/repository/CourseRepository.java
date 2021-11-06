package domain.repository;

import domain.model.Course;

public interface CourseRepository {
    public Course findByID(long id);
    public void save(Course Course);
}
