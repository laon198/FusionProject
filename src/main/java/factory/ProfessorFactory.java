package factory;

import domain.model.Professor;
import dto.ProfessorDTO;

public class ProfessorFactory {
    public Professor create(ProfessorDTO dto){
        return Professor.builder(
                dto.getName(),
                dto.getDepartment(),
                dto.getBirthDate(),
                dto.getProfessorCode())
                .telePhone(dto.getTelePhone())
                .build();
    }
}
