package domain.repository;

import domain.model.Student;
import infra.option.student.StudentOption;

import java.sql.SQLException;
import java.util.List;

public interface StudentRepository {
    public List<Student> findByOption(StudentOption... options);
    public Student findByID(long id);
    public List<Student> findAll();
    public void save(Student student);
}
