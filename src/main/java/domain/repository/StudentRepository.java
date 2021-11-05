package domain.repository;

import domain.model.Student;
import domain.model.StudentID;

public interface StudentRepository {
    public Student findByID(StudentID id);
    public void save(Student student);
}
