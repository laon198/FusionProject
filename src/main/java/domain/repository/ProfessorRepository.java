package domain.repository;

import domain.model.Professor;
<<<<<<< HEAD

public interface ProfessorRepository {
    public Professor findByID(long id);
=======
import domain.model.ProfessorID;

public interface ProfessorRepository {
    public Professor findByID(ProfessorID id);
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    public void save(Professor professor);
}
