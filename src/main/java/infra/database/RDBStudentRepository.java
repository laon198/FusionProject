package infra.database;

import domain.model.Student;
import domain.repository.StudentRepository;

public class RDBStudentRepository implements StudentRepository {
    @Override
    public Student findByID(long id) {
        return null;
    }

    @Override
    public void save(Student student) {
    }
}
