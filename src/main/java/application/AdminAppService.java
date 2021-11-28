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

public class AdminAppService {
    private AdminRepository adminRepo;
    private AccountRepository accRepo;

    public AdminAppService(AdminRepository adminRepo, AccountRepository accRepo) {
        this.adminRepo = adminRepo;
        this.accRepo = accRepo;
    }

    public void create(AdminDTO adminDTO){
        //TODO : 생성시 발리데이션 체크필요
        Admin admin = Admin.builder()
                .name(adminDTO.getName())
                .birthDate(adminDTO.getBirthDate())
                .department(adminDTO.getDepartment())
                .adminCode(adminDTO.getAdminCode())
                .build();

        long adminID = adminRepo.save(admin);

        Account acc = Account.builder()
                .id(adminDTO.getAdminCode())
                .password(adminDTO.getBirthDate())
                .memberID(adminID)
                .build();

        accRepo.save(acc);
    }

    //TODO : partial update 불가
    //TODO : 바뀌면 안되는 값에 대한 처리?
    public void update(AdminDTO adminDTO){
        Admin admin = Admin.builder()
                .id(adminDTO.getId())
                .name(adminDTO.getName())
                .birthDate(adminDTO.getBirthDate())
                .department(adminDTO.getDepartment())
                .adminCode(adminDTO.getAdminCode())
                .build();

        adminRepo.save(admin);
    }

    //TODO : id가 없을때 예외처리
    public void delete(AdminDTO adminDTO){
        Admin admin = Admin.builder().id(adminDTO.getId()).build();

        adminRepo.remove(admin);
    }

    public AdminDTO retrieveByID(long id){
        return ModelMapper.adminToDTO(adminRepo.findByID(id));
    }

    //TODO : 추후 구현
//    public List<AdminDTO> retrieveByOption(StudentOption... options){
//        return stdListToDTOList(stdRepo.findByOption(options));
//    }
//
    //TODO : 비효율적
    public List<AdminDTO> retrieveAll(){
        return adminListToDTOList(adminRepo.findAll());
    }

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
