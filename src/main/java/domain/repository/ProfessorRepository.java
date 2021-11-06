package domain.repository;

import domain.model.Professor;

public interface ProfessorRepository {
    public Professor findByID(long id);
    public void save(Professor professor);
}
