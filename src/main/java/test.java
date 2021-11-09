
import application.MemberAppService;
import application.ProfessorRetrieveAppService;
import application.StudentRetrieveAppService;
import domain.model.Professor;
import domain.model.Student;
import domain.repository.AccountRepository;
import domain.repository.AdminRepository;
import domain.repository.ProfessorRepository;
import domain.repository.StudentRepository;
import infra.database.*;
import infra.dto.ProfessorDTO;

import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        AdminRepository adminRepo = new RDBAdminRepository();
        StudentRepository stdRepo = new RDBStudentRepository();
        ProfessorRepository profRepo = new RDBProfessorRepository();
        AccountRepository accRepo = new RDBAccountRepository();
        MemberAppService m = new MemberAppService(
                stdRepo,
                accRepo,
                profRepo,
                adminRepo
        );
        StudentRetrieveAppService s = new StudentRetrieveAppService(stdRepo);
        ProfessorRetrieveAppService p = new ProfessorRetrieveAppService(profRepo);

        //insert test
//        StudentDTO stdDTO = StudentDTO.builder()
//                .name("jinwoo")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("201812144")
//                .year(2)
//                .build();
//
//        m.createStudent(stdDTO);

//        ProfessorDTO profDTO = ProfessorDTO.builder()
//                .name("oh")
//                .birthDate("601010")
//                .department("SE")
//                .professorCode("PO82002")
//                .telePhone("9000")
//                .build();
//
//        m.createProfessor(profDTO);

//        AdminDTO adminDTO = AdminDTO.builder()
//                .name("hana")
//                .birthDate("001020")
//                .department("SE")
//                .adminCode("F1234")
//                .build();
//        m.createAdmin(adminDTO);

        //update test
//        Student std = s.findByID(44);
//        System.out.println("std = " + std);
//        std.setName("kimJinWoo");
//        m.updateStudent(std);
//        Student std2 = s.findByID(44);
//        System.out.println("std = " + std2);

//        Professor prof = p.findByID(53);
//        System.out.println("prof = " + prof);
//        prof.setTelePhone("8001");
//        m.updateProfessor(prof);
//        Professor prof2 = p.findByID(53);
//        System.out.println("prof = " + prof2);
        
        //find all test
        List<Student> list = s.findAll();
        for(Student std : list){
            System.out.println("std = " + std);
        }
        
//        for(Professor prof : p.findAll()){
//            System.out.println("prof = " + prof);
//        }
    }
}
