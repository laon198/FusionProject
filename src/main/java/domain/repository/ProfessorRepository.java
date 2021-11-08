package domain.repository;

import domain.model.Professor;

import java.util.List;

public interface ProfessorRepository {
    public Professor findByID(long id);
    public List<Professor> findAll();
    public void save(Professor professor);
}
