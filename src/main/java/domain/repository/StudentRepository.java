package domain.repository;

import domain.model.Student;

public interface StudentRepository {
    public Student findByID(long id);
    public void save(Student student);
}
