package application;

import domain.model.Professor;
import domain.repository.ProfessorRepository;

import java.util.List;

public class ProfessorRetrieveAppService {
    ProfessorRepository profRepo;

    public ProfessorRetrieveAppService(
            ProfessorRepository profRepo
    ){
        this.profRepo = profRepo;
    }

    public Professor findByID(long id){
        return profRepo.findByID(id);
    }

    public List<Professor> findAll(){
        return profRepo.findAll();
    }
}
