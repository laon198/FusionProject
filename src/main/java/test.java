
import domain.generic.LectureTime;
import domain.model.Lecture;
import infra.MyBatisConnectionFactory;
import infra.database.RDBLectureRepository;
import infra.dto.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {
    public static void main(String[] args) throws Exception {
//        AdminRepository adminRepo = new RDBAdminRepository();
//        StudentRepository stdRepo = new RDBStudentRepository();
//        ProfessorRepository profRepo = new RDBProfessorRepository();
//        AccountRepository accRepo = new RDBAccountRepository();
//        MemberAppService m = new MemberAppService(
//                stdRepo,
//                accRepo,
//                profRepo,
//                adminRepo
//        );
//        StudentRetrieveAppService s = new StudentRetrieveAppService(stdRepo);
//        ProfessorRetrieveAppService p = new ProfessorRetrieveAppService(profRepo);

        RDBLectureRepository rdbLectureRepository = new RDBLectureRepository(MyBatisConnectionFactory.getSqlSessionFactory());
        List<Lecture> all = rdbLectureRepository.findAll();
//        Set<LectureTime> lectureTimes = new HashSet<>();
//        LectureTime lectureTime = LectureTime.builder()
//                .lectureDay("MON")
//                .startTime(1)
//                .endTime(2)
//                .room("D333")
//                .build();
//        lectureTimes.add(lectureTime);
//        Lecture lecture = Lecture.builder()
//                .id(14)
//                .lecturerID("p333")
//                .lectureCode("SE456")
//                .lectureTimes(lectureTimes)
//                .courseID(1)
//                .limit(60)
//                .build();
//        rdbLectureRepository.insert(lecture);

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
//        List<Student> list = s.findAll();
//        for(Student std : list){
//            System.out.println("std = " + std);
//        }

//        for(Professor prof : p.findAll()){
//            System.out.println("prof = " + prof);
//        }
    }
}
