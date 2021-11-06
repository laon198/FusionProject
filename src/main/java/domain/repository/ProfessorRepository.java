package domain.repository;

import domain.model.Professor;
import domain.model.ProfessorID;

public interface ProfessorRepository {
    public Professor findByID(long id);
    public void save(Professor professor);
}
