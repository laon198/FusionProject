package domain.repository;

import domain.model.Student;
<<<<<<< HEAD

public interface StudentRepository {
    public Student findByID(long id);
=======
import domain.model.StudentID;

public interface StudentRepository {
    public Student findByID(StudentID id);
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    public void save(Student student);
}
