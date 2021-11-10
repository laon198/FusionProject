
import application.MemberAppService;
import application.ProfessorRetrieveAppService;
import application.RegisterAppService;
import application.StudentRetrieveAppService;
import domain.model.*;
import domain.repository.*;
import domain.service.Registrar;
import infra.MyBatisConnectionFactory;
import infra.database.*;
import infra.dto.*;
import infra.option.registering.StudentCodeOption;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {
    public static void main(String[] args){
        AdminRepository adminRepo = new RDBAdminRepository();
        LectureRepository lectureRepo = new RDBLectureRepository(MyBatisConnectionFactory.getSqlSessionFactory());
        CourseRepository courseRepo = new RDBCourseRepository(MyBatisConnectionFactory.getSqlSessionFactory());
        RegisteringRepository regRepo = new RDBRegisteringRepository();
        RegPeriodRepository periodRepo = new RDBRegPeriodRepository();
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
        RegisterAppService r = new RegisterAppService(lectureRepo, stdRepo, courseRepo, regRepo, periodRepo);

        //회원 Create
        //create Admin
//        AdminDTO adminDTO = AdminDTO.builder()
//                .name("leehana")
//                .birthDate("001020")
//                .department("SE")
//                .adminCode("F1234")
//                .build();
//        m.createAdmin(adminDTO);
        //end of admin create

        //create professor
//        ProfessorDTO profDTO = ProfessorDTO.builder()
//                .name("kimsungryul")
//                .birthDate("991020")
//                .department("SE")
//                .professorCode("P1000")
//                .telePhone("9000")
//                .build();
//
//        m.createProfessor(profDTO);
//
//        ProfessorDTO profDTO2 = ProfessorDTO.builder()
//                .name("kimsunmyeong")
//                .birthDate("601010")
//                .department("SE")
//                .professorCode("P2000")
//                .telePhone("7000")
//                .build();
//
//        m.createProfessor(profDTO2);
        //end of professor create

        //create student
//        StudentDTO stdDTO = StudentDTO.builder()
//                .name("kimjinwoo")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("20180303")
//                .year(2)
//                .build();
//
//        m.createStudent(stdDTO);
//
//        StudentDTO stdDTO2 = StudentDTO.builder()
//                .name("parkhyeongjun")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("20181111")
//                .year(2)
//                .build();
//
//        m.createStudent(stdDTO2);
//
//        StudentDTO stdDTO3 = StudentDTO.builder()
//                .name("leeeunbean")
//                .birthDate("000312")
//                .department("SE")
//                .studentCode("20182222")
//                .year(2)
//                .build();
//
//        m.createStudent(stdDTO3);
//
//        StudentDTO stdDTO4 = StudentDTO.builder()
//                .name("yeongeomji")
//                .birthDate("990429")
//                .department("SE")
//                .studentCode("20183333")
//                .year(2)
//                .build();
//
//        m.createStudent(stdDTO4);
        //end of student create

        //retrieve all professor
//        for(Professor prof : p.findAll()){
//            System.out.println("prof = " + prof);
//        }
        //end of retrieve professor

        //retrieve all student
//        for(Student std : s.findAll()){
//            System.out.println("std = " + std);
//        }
        //end of retrieve professor

        //update professor
        //TODO : 아이디값 지정 필요
//        Professor prof = p.findByID(53);
//        System.out.println("prof = " + prof);
//        prof.setTelePhone("8001");
//        m.updateProfessor(prof);
//        Professor prof2 = p.findByID(53);
//        System.out.println("prof = " + prof2);
        //end of update professor

        //update student
        //TODO : 아이디값 지정 필요
//        Student std = s.findByID(44);
//        System.out.println("std = " + std);
//        std.setName("kimJinWoo");
//        m.updateStudent(std);
//        Student std2 = s.findByID(44);
//        System.out.println("std = " + std2);
        //end of update student

        //교과목 생성 테스트
        //2학년 2학기 과목
//        Course c1 = Course.builder()
//                .courseName("C++프로그래밍")
//                .courseCode("CS0077")
//                .department("SE")
//                .targetYear(2)
//                .credit(3)
//                .build();
//
//        Course c2 = Course.builder()
//                .courseName("운영체제")
//                .courseCode("CS0017")
//                .department("SE")
//                .targetYear(2)
//                .credit(3)
//                .build();
//
//        Course c3 = Course.builder()
//                .courseName("컴퓨터네트워크")
//                .courseCode("CS0016")
//                .department("SE")
//                .targetYear(2)
//                .credit(4)
//                .build();
//
//        Course c4 = Course.builder()
//                .courseName("융합프로젝트")
//                .courseCode("CS0069")
//                .department("SE")
//                .targetYear(2)
//                .credit(2)
//                .build();
//
//        Course c5 = Course.builder()
//                .courseName("오픈소스소프트웨어")
//                .courseCode("CS0080")
//                .department("SE")
//                .targetYear(2)
//                .credit(2)
//                .build();
//        //1학년 2학기
//        Course c6 = Course.builder()
//                .courseName("자바프로그래밍")
//                .courseCode("CS0010")
//                .department("SE")
//                .targetYear(1)
//                .credit(3)
//                .build();
//        //3학년 2학기
//        Course c7 = Course.builder()
//                .courseName("디자인패턴")
//                .courseCode("CS0027")
//                .department("SE")
//                .targetYear(3)
//                .credit(3)
//                .build();
//        //4학년 2학기
//        Course c8 = Course.builder()
//                .courseName("컴파일러")
//                .courseCode("CS0035")
//                .department("SE")
//                .targetYear(4)
//                .credit(3)
//                .build();
//
//        courseRepo.insert(c1);
//        courseRepo.insert(c2);
//        courseRepo.insert(c3);
//        courseRepo.insert(c4);
//        courseRepo.insert(c5);
//        courseRepo.insert(c6);
//        courseRepo.insert(c7);
//        courseRepo.insert(c8);
        //end of course create
        
        //find all course
//        for(Course course : courseRepo.findAll()){
//            System.out.println("course = " + course);
//        }
        //end of find all course

        //update course name
        //TODO : 아이디값 어떻게?
//        Course updatingCourse = courseRepo.findByID(5);
//        updatingCourse.setCourseName("리눅스의 이해");
//        courseRepo.save(updatingCourse);
        //end of update course name


        //수강신청 기간설정
//        Student currentStd = stdRepo.findByID(61);
//        //TODO : 아이디값 어떻게?
//        for(Lecture lecture : lectureRepo.findAll()){
//            Course curCourse = courseRepo.findByID(lecture.getCourseID());
//            System.out.print("lecture = " + lecture);
//            System.out.print(" targetYear = "+curCourse.getYear());
//
//            if(r.isValidPeriodAbout(currentStd, curCourse)){
//                System.out.println(" : 수강신청 가능");
//            }else{
//                System.out.println(" : 수강신청 불가");
//            }
//        }
//        System.out.println();
//
//        RegisteringPeriodDTO rPeriod = RegisteringPeriodDTO.builder()
//                .period(
//                        PeriodDTO.builder()
//                                .beginTime(LocalDateTime.of(2021,11,10,00,00))
//                                .endTime(LocalDateTime.of(2021,12,10,00,00))
//                                .build())
//                .allowedYear(2)
//                .build();
//
//        r.addRegisteringPeriod(rPeriod);
//
//        for(Lecture lecture : lectureRepo.findAll()){
//            Course curCourse = courseRepo.findByID(lecture.getCourseID());
//            System.out.print("lecture = " + lecture);
//            System.out.print(" targetYear = "+curCourse.getYear());
//
//            if(r.isValidPeriodAbout(currentStd, curCourse)){
//                System.out.println(" : 수강신청 가능");
//            }else{
//                System.out.println(" : 수강신청 불가");
//            }
//        }

        //수강신청 기간설정 끝

        //수강신청
        //TODO: 아이디값
//        r.register(26, 61);
//        List<Registering> list = regRepo.findByOption(new StudentCodeOption("20180303"));
        r.cancel(1, 26, 61);

        //수강신청 끝

//        r.register(15, 2);
//        Student std = stdRepo.findByID(2);
//        System.out.println("std = " + std);

//        for(Registering reg : list){
//            System.out.println("reg = " + reg);

//        }
//
//        r.cancel(6, 15, 2);
    }
}
