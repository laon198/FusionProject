package infra.database;

import domain.model.Professor;
<<<<<<< HEAD
=======
import domain.model.ProfessorID;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
import domain.repository.ProfessorRepository;

import java.util.HashMap;
import java.util.Map;

public class SimpleProfessorRepository implements ProfessorRepository {
<<<<<<< HEAD
    private Map<Long, Professor> repo = new HashMap<>();

    @Override
    public Professor findByID(long id) {
=======
    private Map<ProfessorID, Professor> repo = new HashMap<>();

    @Override
    public Professor findByID(ProfessorID id) {
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        return repo.get(id);
    }

    @Override
    public void save(Professor professor) {
        repo.put(professor.getId(), professor);
    }
}
