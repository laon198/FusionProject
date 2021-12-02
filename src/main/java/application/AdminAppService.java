package application;

import domain.model.Account;
import domain.model.Admin;
import domain.repository.AccountRepository;
import domain.repository.AdminRepository;
import infra.database.option.student.StudentOption;
import infra.dto.ModelMapper;
import infra.dto.AdminDTO;

import java.util.ArrayList;
import java.util.List;

//관리자와 관련된 기능을 수행하는 객체
public class AdminAppService {
    private AdminRepository adminRepo;
    private AccountRepository accRepo;

    public AdminAppService(AdminRepository adminRepo, AccountRepository accRepo) {
        this.adminRepo = adminRepo;
        this.accRepo = accRepo;
    }

    //관리자 계정 생성 기능
    public void create(AdminDTO adminDTO){
        //통신을통해 받은 정보로 관리자 생성
        Admin admin = Admin.builder()
                .name(adminDTO.getName())
                .birthDate(adminDTO.getBirthDate())
                .department(adminDTO.getDepartment())
                .adminCode(adminDTO.getAdminCode())
                .build();

        //관리자 데이터베이스에 저장
        long adminID = adminRepo.save(admin);

        //생성된 관리자 정보 기반으로 계정 생성
        Account acc = Account.builder()
                .id(adminDTO.getAdminCode())
                .password(adminDTO.getBirthDate())
                .memberID(adminID)
                .position("ADMIN")
                .build();

        //계정 데이터베이스에 저장
        accRepo.save(acc);
    }

    //관리자 정보 수정 기능
    public void update(AdminDTO adminDTO){
        //받은 정보로 업데이트할 관리자 생성
        Admin admin = Admin.builder()
                .id(adminDTO.getId())
                .name(adminDTO.getName())
                .birthDate(adminDTO.getBirthDate())
                .department(adminDTO.getDepartment())
                .adminCode(adminDTO.getAdminCode())
                .build();

        //관리자 정보 수정
        adminRepo.save(admin);
    }

    //관리자 삭제 기능
    public void delete(AdminDTO adminDTO){
        //받은 정보로 삭제할 관리자 생성
        Admin admin = Admin.builder().id(adminDTO.getId()).build();

        //관리자 삭제
        adminRepo.remove(admin);
    }

    //ID로 관리자 조회 기능
    public AdminDTO retrieveByID(long id){
        //관리자를 조회후 DTO로 변환하여 반환
        return ModelMapper.adminToDTO(adminRepo.findByID(id));
    }

    //ID로 관리자 조회 기능
    public List<AdminDTO> retrieveAll(){
        //관리자 전체를 조회후 DTO로 변환하여 반환
        return adminListToDTOList(adminRepo.findAll());
    }

    //관리자 list를 관리자DTO 리스트로 변환
    private List<AdminDTO> adminListToDTOList(List<Admin> adminList){
        List<AdminDTO> list = new ArrayList<>();

        for(Admin admin : adminList){
            list.add(
                    ModelMapper.adminToDTO(admin)
            );
        }

        return list;
    }
}
