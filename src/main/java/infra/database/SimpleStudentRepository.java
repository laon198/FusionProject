package infra.database;

import domain.model.Student;
import domain.model.StudentID;
import domain.repository.StudentRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleStudentRepository implements StudentRepository {
    private Map<StudentID, Student> repo = new HashMap<>();

    public Student findByID(StudentID id) {
        return repo.get(id);
    }

    public void save(Student student) {
        repo.put(student.getID(), student);
    }
}
