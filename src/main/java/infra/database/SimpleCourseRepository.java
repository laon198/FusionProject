package infra.database;

import domain.model.Course;
<<<<<<< HEAD
=======
import domain.model.CourseID;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
import domain.repository.CourseRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleCourseRepository implements CourseRepository {
<<<<<<< HEAD
    private Map<Long, Course> repo = new HashMap<>();

    @Override
    public Course findByID(long id) {
=======
    private Map<CourseID, Course> repo = new HashMap<>();

    @Override
    public Course findByID(CourseID id) {
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        return repo.get(id);
    }

    public void save(Course course){
        repo.put(course.getId(), course);
    }
}
