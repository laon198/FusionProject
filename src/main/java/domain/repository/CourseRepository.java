package domain.repository;

import domain.model.Course;
import infra.option.course.CourseOption;

import java.util.List;

public interface CourseRepository {
    public List<Course> findByOption(CourseOption... option);
    public Course findByID(long id);
    public void save(Course Course);
    public void insert(Course course);
}
