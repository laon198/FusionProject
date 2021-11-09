package infra.database;

import domain.model.Professor;
import domain.repository.ProfessorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleProfessorRepository implements ProfessorRepository {
    private Map<Long, Professor> repo = new HashMap<>();

    @Override
    public Professor findByID(long id) {
        return repo.get(id);
    }

    @Override
    public List<Professor> findAll() {
        return null;
    }

    @Override
    public long save(Professor professor) {
        repo.put(professor.getID(), professor);
        return 1;
    }
}
