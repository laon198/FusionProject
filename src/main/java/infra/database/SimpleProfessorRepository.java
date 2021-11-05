package infra.database;

import domain.model.Professor;
import domain.model.ProfessorID;
import domain.repository.ProfessorRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleProfessorRepository implements ProfessorRepository {
    private Map<ProfessorID, Professor> repo = new HashMap<>();

    @Override
    public Professor findByID(ProfessorID id) {
        return repo.get(id);
    }

    @Override
    public void save(Professor professor) {
        repo.put(professor.getId(), professor);
    }
}
