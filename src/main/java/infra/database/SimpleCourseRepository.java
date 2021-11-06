package infra.database;

import domain.model.Course;
import domain.repository.CourseRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleCourseRepository implements CourseRepository {
    private Map<Long, Course> repo = new HashMap<>();

    @Override
    public Course findByID(long id) {
        return repo.get(id);
    }

    public void save(Course course){
        repo.put(course.getId(), course);
    }
}
