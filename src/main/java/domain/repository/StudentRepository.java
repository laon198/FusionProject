package domain.repository;

import domain.model.Student;
import infra.database.option.student.StudentOption;

import java.util.List;

public interface StudentRepository {
    public List<Student> findByOption(StudentOption... options);
    public Student findByID(long id);
    public List<Student> findAll();
    public long save(Student student);
    public void remove(Student student);
}
