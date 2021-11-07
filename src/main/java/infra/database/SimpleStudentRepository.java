package infra.database;

import domain.model.Student;
import domain.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleStudentRepository implements StudentRepository {
    private Map<Long, Student> repo = new HashMap<>();

    public Student findByID(long id) {
        return repo.get(id);
    }

    @Override
    public List<Student> findAll() {
        return null;
    }


    public void save(Student student) {
        repo.put(student.getID(), student);
    }
}
