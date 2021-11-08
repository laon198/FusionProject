package domain.repository;

import domain.model.Course;
import infra.option.Option;

import java.util.List;

public interface CourseRepository {
    public List<Course> findByOption(Option... option);
    public Course findByID(long id);
    public List<Course> findByYear(int year);
    public void save(Course Course);
    public void insert(Course course);
}
