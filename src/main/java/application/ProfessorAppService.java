package application;

import domain.model.Account;
import domain.model.Professor;
import domain.repository.AccountRepository;
import domain.repository.ProfessorRepository;
import infra.database.option.professor.ProfessorOption;
import infra.dto.ModelMapper;
import infra.dto.ProfessorDTO;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAppService {
    private ProfessorRepository profRepo;
    private AccountRepository accRepo;

    public ProfessorAppService(ProfessorRepository profRepo, AccountRepository accRepo) {
        this.profRepo = profRepo;
        this.accRepo = accRepo;
    }

    public void create(ProfessorDTO profDTO) {
        //TODO : 생성시 validation 로직 필요
        Professor prof = Professor.builder()
                .name(profDTO.getName())
                .birthDate(profDTO.getBirthDate())
                .department(profDTO.getDepartment())
                .professorCode(profDTO.getProfessorCode())
                .telePhone(profDTO.getTelePhone())
                .build();

        long profID = profRepo.save(prof);

        Account acc = Account.builder()
                .id(profDTO.getProfessorCode())
                .password(profDTO.getBirthDate())
                .memberID(profID)
                .build();

        accRepo.save(acc);
    }

    //TODO : partial update 불가
    //TODO : 바뀌면 안되는 값에 대한 처리?
    public void update(ProfessorDTO profDTO) {
        Professor prof = profRepo.findByID(profDTO.getId());

        //TODO : 교수 업데이트 되는 항목 추가필요

        profRepo.save(prof);
    }

    //TODO : id가 없을때 예외처리
    public void delete(ProfessorDTO profDTO) {
        Professor prof = Professor.builder().id(profDTO.getId()).build();

        profRepo.remove(prof);
    }

    public ProfessorDTO retrieveByID(long id) {
        return ModelMapper.professorToDTO(profRepo.findByID(id));
    }

    //TODO : 추후구현
    public ProfessorDTO[] retrieveByOption(ProfessorOption...options) {
        return profListToDTOArr(profRepo.findByOption(options));
    }


    //TODO : 비효율적
    public ProfessorDTO[] retrieveAll() {
        return profListToDTOArr(profRepo.findAll());
    }

    private ProfessorDTO[] profListToDTOArr(List<Professor> profList) {
        ProfessorDTO[] arr = new ProfessorDTO[profList.size()];

        for(int i=0; i<arr.length; i++){
            arr[i] = ModelMapper.professorToDTO(profList.get(i));
        }

        return arr;
    }

}