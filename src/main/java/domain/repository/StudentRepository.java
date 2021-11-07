package domain.repository;

import domain.model.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentRepository {
    public Student findByID(long id);
    public List<Student> findAll();
    public void save(Student student);
}
