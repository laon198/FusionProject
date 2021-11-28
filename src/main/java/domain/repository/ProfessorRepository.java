package domain.repository;

import domain.model.Professor;
import infra.database.option.professor.ProfessorOption;

import java.util.List;

public interface ProfessorRepository {
    public Professor findByID(long id);
    public List<Professor> findAll();
    public List<Professor> findByOption(ProfessorOption... options);
    public long save(Professor professor);
    public void remove(Professor professor);
}
