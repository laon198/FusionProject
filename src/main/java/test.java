
import application.*;
import domain.generic.LectureTime;
import domain.model.*;
import domain.repository.*;
import infra.database.MyBatisConnectionFactory;
import infra.database.option.account.AccountIDOption;
import infra.database.option.lecture.LectureCodeOption;
import infra.database.option.lecture.LectureDepartmentOption;
import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.ProfessorCodeOption;
import infra.database.repository.*;
import infra.dto.*;
import infra.network.Deserializer;
import infra.network.Serializer;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static domain.generic.LectureTime.DayOfWeek.FRI;
import static domain.generic.LectureTime.DayOfWeek.WED;

public class test {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        LectureRepository lectureRepo = new RDBLectureRepository();
//        Course[] courses = new Course[3];
//        courses[0] = Course.builder()
//                    .id(1)
//                    .courseCode("111")
//                .courseName("hello1")
//                .department("4r1")
//                    .targetYear(21)
//                .credit(31)
//                .build();
//        courses[1] = Course.builder()
//                .id(2)
//                .courseCode("2222")
//                .courseName("hello2")
//                .department("4r2")
//                .targetYear(22)
//                .credit(33)
//                .build();
//        courses[2] = Course.builder()
//                .id(3)
//                .courseCode("3333")
//                .courseName("3323")
//                .department("333")
//                .targetYear(2)
//                .credit(3)
//                .build();
//
//        try{
//            //서버
//            byte[] packets = Serializer.objectArrToBytes(courses);
//            //클라이언트
//            Course[] c2 =  (Course[]) Deserializer.bytesToObjectArr(packets);
//
//            for(Object c : c2){
//                System.out.println("c = " + (Course)c);
//            }
//        }catch(Exception e){
//            System.out.println("e.getStackTrace() = " + e.getStackTrace());
//            System.out.println("e = " + e);
//        }




//        for(java.lang.reflect.Field f : Base.class.getDeclaredFields()){
//            System.out.println("f.getName() = " + f.getType());
//        }
        
        
//        Refs r = new Refs('d', 5, new Refs2(2, 3));
//        Derived c = new Derived(1, 2, 3, "4ff", r);
//        byte[] a = Serializer.objectToBytes(c);
//        Derived c2 = (Derived) Deserializer.bytesToObject(a);
//        System.out.println("c2 = " + c2);
        
//        for(Field f : Serializer.getAllFields(ArrayList.class)){
//            System.out.println("f = " + f);
//        }

//        Class clazz = Class.forName("infra.dto.CourseDTO");
//
//        Constructor c = clazz.getDeclaredConstructor();
//        c.setAccessible(true);
//        CourseDTO courseDTO = (CourseDTO) c.newInstance();
//        Field f = clazz.getDeclaredField("id");
//        f.setAccessible(true);
//        f.setLong(courseDTO, 3);
//        System.out.println("courseDTO = " + courseDTO);
//        String n = Object.class.getName();
//        System.out.println("n = " + n);
//        List<Integer> a = new ArrayList<>();
//        a.add(2); a.add(3);
//
//        int[] a = new int[3];
//        a[0] = 1;



//        Field f = Course.class.getDeclaredField("courseCode");
//        f.setAccessible(true);
//        String a = new String((String)f.get(c));
//        System.out.println("a = " + a);
//        System.out.println(f.getType().getSimpleName());
//        System.out.println("name = " + name);
//        for(Field f : Serializer.getAllFields(Student.class)){
//            System.out.println("f = " + f.getType().getSimpleName().equals("int"));
//        }
//
//        Set<LectureTimeDTO> times = new HashSet<>();
//        times.add(
//                LectureTimeDTO.builder()
//                    .lectureDay(WED)
//                    .startTime(LectureTime.LecturePeriod.FIRST)
//                    .endTime(LectureTime.LecturePeriod.SECOND)
//                    .room("D331")
//                    .build()
//        );
//
//        LectureAppService a = new LectureAppService(lectureRepo);

//        LectureDTO l = LectureDTO.builder()
//                    .id(26)
//                    .courseID(5)
//                    .lectureCode("SE1244")
//                    .lecturerID("P1000")
//                    .limit(10)
//                    .lectureTimes(times)
//                    .build();
//        a.delete(l);
//        lectureRepo.insert(l);

//        Lecture l = lectureRepo.findByID(23);
//        System.out.println("l = " + l);
//        for(Lecture l : lectureRepo.findAll()){
//            System.out.println("l = " + l);
//        }

        AdminRepository adminRepo = new RDBAdminRepository();
//        LectureRepository lectureRepo = new RDBLectureRepository();
        CourseRepository courseRepo = new RDBCourseRepository();
        RegisteringRepository regRepo = new RDBRegisteringRepository();
        RegPeriodRepository periodRepo = new RDBRegPeriodRepository();
        StudentRepository stdRepo = new RDBStudentRepository();
        ProfessorRepository profRepo = new RDBProfessorRepository();
        AccountRepository accRepo = new RDBAccountRepository();
        StudentAppService stdService = new StudentAppService(
                stdRepo, accRepo
        );

        ProfessorAppService profService = new ProfessorAppService(
                profRepo, accRepo
        );

        AdminAppService adminService = new AdminAppService(
                adminRepo, accRepo
        );

        CourseAppService courseService = new CourseAppService(
                courseRepo
        );

        RegisterAppService r = new RegisterAppService(lectureRepo, stdRepo, courseRepo, regRepo, periodRepo);

        //회원 Create
        //create Admin
//        AdminDTO adminDTO = AdminDTO.builder()
//                .name("leehana4")
//                .birthDate("001020")
//                .department("SE")
//                .adminCode("F1234")
//                .build();
//        adminService.create(adminDTO);
        //end of admin create

        List<Professor> profList = profRepo.findAll();
//        List<ProfessorDTO> dtoList = new ArrayList<>();
        ProfessorDTO[] dtos = new ProfessorDTO[profList.size()];
        int cnt = 0;
        for(Professor p : profList){
            dtos[cnt++] = ModelMapper.professorToDTO(p);
        }

        
        try{
            byte[] packets = Serializer.objectArrToBytes(dtos);
            ProfessorDTO[] p = (ProfessorDTO[]) Deserializer.bytesToObjectArr(packets);

            for(ProfessorDTO p2 : p){
                System.out.println("p2 = " + p2);
            }
        }catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
        }


        //create professor
//        ProfessorDTO profDTO = ProfessorDTO.builder()
//                .name("kimsungryul")
//                .birthDate("991020")
//                .department("SE")
//                .professorCode("P1000")
//                .telePhone("9000")
//                .build();
//
//        profService.create(profDTO);
//
//        ProfessorDTO profDTO2 = ProfessorDTO.builder()
//                .id(85)
//                .name("kimsunmyeong")
//                .birthDate("601010")
//                .department("AE")
//                .professorCode("P2000")
//                .telePhone("7000")
//                .build();
//
//        profService.create(profDTO2);
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
//        stdService.create(stdDTO);

//        StudentDTO l = StudentDTO.builder().id(77).build();
//        stdService.delete(l);
        
//        StudentDTO std = stdService.retrieveByID(78);
//        System.out.println("std = " + std);
        
//        List<StudentDTO> list = stdService.retrieveAll();
//        for(StudentDTO std : list){
//            System.out.println("std = " + std);
//        }

//        List<StudentDTO> list = stdService.retrieveByOption(new StudentYearOption("3"));
//        for(StudentDTO std : list){
//            System.out.println("std = " + std);
//        }

//
//        StudentDTO stdDTO2 = StudentDTO.builder()
//                .name("parkhyeongjun")
//                .birthDate("990329")
//                .department("SE")
//                .studentCode("20181111")
//                .year(2)
//                .build();
//
//        stdService.create(stdDTO2);
//
//        StudentDTO stdDTO3 = StudentDTO.builder()
//                .name("leeeunbean")
//                .birthDate("000312")
//                .department("SE")
//                .studentCode("20182222")
//                .year(2)
//                .build();
//
//        stdService.create(stdDTO3);
//
//        StudentDTO stdDTO4 = StudentDTO.builder()
//                .name("yeongeomji")
//                .birthDate("990429")
//                .department("SE")
//                .studentCode("20183333")
//                .year(3)
//                .build();
//
//        stdService.create(stdDTO4);
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
//        CourseDTO c1 = CourseDTO.builder()
//                .id(10)
//                .courseName("C++프로그래밍")
//                .courseCode("CS0077")
//                .department("SE")
//                .targetYear(2)
//                .credit(3)
//                .build();
//        courseService.create(c1);
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
//        CourseDTO c = courseService.RetrieveByID(2);
//        System.out.println("c = " + c);
        //end of find all course

        //update course name
        //TODO : 아이디값 어떻게?
//        Course updatingCourse = courseRepo.findByID(5);
//        updatingCourse.setCourseName("리눅스의 이해");
//        courseRepo.save(updatingCourse);
        //end of update course name

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

        //수강신청 기간설정
//        Student currentStd = stdRepo.findByID(78);
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
//        r.cancel(1, 26, 61);

        //수강신청 끝

//        try{
//            r.cancel(11, 25 ,"20180303");
//            r.register(23, "20180303");
//            r.cancel(15, 23,  "20180303");
//            r.register(25, "20182222");
//        }catch(IllegalArgumentException | IllegalStateException e){
//            System.out.println(e.getMessage());
//        }

//        try{
//            AccountAppService accService = new AccountAppService(accRepo);
//            AccountDTO accDTO = AccountDTO.builder().id("20180303").password("1234").build();
//            accService.changePassword(accDTO);
//        }catch(IllegalArgumentException e){
//            System.out.println(e.getMessage());
//        }

//        List<Lecture> le = lectureRepo.findByOption(
//                new LectureOption[]{
//                        new ProfessorCodeOption("P1000"),
//                        new LectureCodeOption("SE1235")
//                }
//        );
//        for(Lecture la : le){
//            System.out.println("la = " + la);
//        }
//        lectureRepo.findByID(23);

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
