package application;

import domain.model.Account;
import domain.model.Professor;
import domain.repository.AccountRepository;
import domain.repository.ProfessorRepository;
import factory.AccountFactory;
import factory.ProfessorFactory;
import infra.database.option.professor.ProfessorOption;
import dto.ModelMapper;
import dto.ProfessorDTO;

import java.util.List;

//교수와 관련된 기능을 수행하는 객체
public class ProfessorAppService {
    private final ProfessorRepository profRepo;
    private final AccountRepository accRepo;
    private final ProfessorFactory profFactory;
    private final AccountFactory accountFactory;

    public ProfessorAppService(ProfessorRepository profRepo, AccountRepository accRepo) {
        this.profRepo = profRepo;
        this.accRepo = accRepo;
        profFactory = new ProfessorFactory();
        accountFactory = new AccountFactory();
    }

    //교수 생성 기능
    public void create(ProfessorDTO profDTO) {
        //받은 DTO로 교수 객체 생성
        Professor prof = profFactory.create(profDTO);

        //교수 객체 데이터베이스 저장
        long profID = profRepo.save(prof);

        //교수 정보로 계정 생성
        Account acc = accountFactory.create(profDTO, profID, "PROF");

        //계정 데이터베이스에 저장
        accRepo.save(acc);
    }

    //교수 정보 수정 기능
    public void update(ProfessorDTO profDTO) {
        //받은 정보로 교수 객체 조회
        Professor prof = profRepo.findByID(profDTO.getId());

        //교수객체에 값변경
        prof.setName(profDTO.getName());
        prof.setTelePhone(profDTO.getTelePhone());

        //데이터베이스에 변경된 교수 저장
        profRepo.save(prof);
    }

    //교수 삭제 기능
    public void delete(ProfessorDTO profDTO) {
        //교수객체 생성(삭제될 교수의 id값만 가지는)
        Professor prof = Professor.builder().id(profDTO.getId()).build();

        //데이터베이스에 교수 삭제
        profRepo.remove(prof);
    }

    //id로 교수 조회 기능
    public ProfessorDTO retrieveByID(long id) {
        //id값에 해당하는 교수 조회후 DTO로 반환
        return ModelMapper.professorToDTO(profRepo.findByID(id));
    }

    //조건으로 교수 조회
    public ProfessorDTO[] retrieveByOption(ProfessorOption...options) {
        //조건으로 교수 조회해서 DTO배열로 반환
        return profListToDTOArr(profRepo.findByOption(options));
    }


    //교수 전체 조회
    public ProfessorDTO[] retrieveAll() {
        //교수 전체 조회해서 DTO배열로 반환
        return profListToDTOArr(profRepo.findAll());
    }

    //교수 list를 DTO배열로 변환
    private ProfessorDTO[] profListToDTOArr(List<Professor> profList) {
        ProfessorDTO[] arr = new ProfessorDTO[profList.size()];

        for(int i=0; i<arr.length; i++){
            arr[i] = ModelMapper.professorToDTO(profList.get(i));
        }

        return arr;
    }

}