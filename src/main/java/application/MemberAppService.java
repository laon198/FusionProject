package application;

import domain.model.Account;
import domain.model.Admin;
import domain.model.Professor;
import domain.model.Student;
import domain.repository.*;
import domain.service.MemberManageService;
import infra.dto.AdminDTO;
import infra.dto.ProfessorDTO;
import infra.dto.StudentDTO;

public class MemberAppService {
    private StudentRepository stdRepo;
    private AccountRepository accRepo;
    private ProfessorRepository profRepo;
    private AdminRepository adminRepo;

    public MemberAppService(
            StudentRepository stdRepo,
            AccountRepository accRepo,
            ProfessorRepository profRepo,
            AdminRepository adminRepo
    ){
        this.stdRepo = stdRepo;
        this.accRepo = accRepo;
        this.profRepo = profRepo;
        this.adminRepo = adminRepo;
    }

    public void createStudent(StudentDTO stdDTO){
        MemberManageService memberService = new MemberManageService();
        Student std = memberService.createStudent(
                stdDTO.getName(),
                stdDTO.getBirthDate(),
                stdDTO.getDepartment(),
                stdDTO.getYear(),
                stdDTO.getStudentCode()
        );

        long stdID = stdRepo.save(std);

        Account acc = memberService.createAccount(
                stdDTO.getStudentCode(), stdDTO.getBirthDate(), stdID
        );

        accRepo.save(acc);
    }

    public void createProfessor(ProfessorDTO profDTO){
        MemberManageService memberService = new MemberManageService();
        Professor prof = memberService.createProfessor(
                profDTO.getName(),
                profDTO.getBirthDate(),
                profDTO.getDepartment(),
                profDTO.getProfessorCode(),
                profDTO.getTelePhone()
        );

        long profID = profRepo.save(prof);

        Account acc = memberService.createAccount(
                profDTO.getProfessorCode(), profDTO.getBirthDate(), profID
        );

        accRepo.save(acc);
    }

    public void createAdmin(AdminDTO adminDTO){
        MemberManageService memberService = new MemberManageService();
        Admin admin = memberService.createAdmin(
                adminDTO.getName(),
                adminDTO.getBirthDate(),
                adminDTO.getDepartment(),
                adminDTO.getAdminCode()
        );

        long adminID = adminRepo.save(admin);

        Account acc = memberService.createAccount(
                adminDTO.getAdminCode(), adminDTO.getBirthDate(), adminID
        );

        accRepo.save(acc);
    }

    public void updateStudent(Student std){
        stdRepo.save(std);
    }

    public void updateProfessor(Professor prof){
        profRepo.save(prof);
    }

    //TODO : 아직 테스트 안해봄
    public  void updateAdmin(Admin admin){
        adminRepo.save(admin);
    }
}