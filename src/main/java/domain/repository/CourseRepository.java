package domain.repository;

import domain.model.Course;
import infra.database.option.course.CourseOption;

import java.util.List;

public interface CourseRepository {
    public List<Course> findByOption(CourseOption... option);
    public Course findByID(long id);
    public List<Course> findAll();
    public List<Course> findByYear(int year);
    public void save(Course Course);
    public void remove(Course Course);
}
