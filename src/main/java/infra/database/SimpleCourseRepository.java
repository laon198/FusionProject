package infra.database;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.option.Option;
import infra.option.course.CourseOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCourseRepository implements CourseRepository {
    private Map<Long, Course> repo = new HashMap<>();

    public List<Course> findByOption(Option... option) {
        return null;
    }

    @Override
    public List<Course> findByOption(CourseOption... option) {
        return null;
    }

    @Override
    public Course findByID(long id) {
        return repo.get(id);
    }

    @Override
    public List<Course> findByYear(int year) {
        return null;
    }

    public void save(Course course){
        repo.put(course.getId(), course);
    }

    @Override
    public void insert(Course course) {

    }
}
