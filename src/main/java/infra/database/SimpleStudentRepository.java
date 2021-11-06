package infra.database;

import domain.model.Student;
<<<<<<< HEAD
=======
import domain.model.StudentID;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
import domain.repository.StudentRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleStudentRepository implements StudentRepository {
<<<<<<< HEAD
    private Map<Long, Student> repo = new HashMap<>();

    public Student findByID(long id) {
=======
    private Map<StudentID, Student> repo = new HashMap<>();

    public Student findByID(StudentID id) {
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        return repo.get(id);
    }

    public void save(Student student) {
        repo.put(student.getID(), student);
    }
}
