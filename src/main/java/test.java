
import domain.model.Course;
import domain.model.Lecture;
import domain.model.Professor;
import domain.model.Student;
import domain.repository.ProfessorRepository;
import domain.repository.StudentRepository;
import infra.MyBatisConnectionFactory;
import infra.database.*;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;
import infra.option.course.CourseCodeOption;
import infra.option.student.StudentCodeOption;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        ProfessorRepository profRepo = new RDBProfessorRepository();
        profRepo.save(
                Professor.builder()
                .id(31)
                .name("kim")
                .department("SE")
                .birthDate("0329")
                .professorCode("P1555")
                .build()
        );
//        StudentRepository stdRepo = new RDBStudentRepository();
//        Student std = stdRepo.findByOption(new StudentCodeOption("20180603")).get(0);
//        System.out.println(std);

        RDBLectureRepository rdbLectureRepository = new RDBLectureRepository(MyBatisConnectionFactory.getSqlSessionFactory());
        List<Lecture> all = rdbLectureRepository.findAll();

        // Course test
        RDBCourseRepository rdbCourseRepository = new RDBCourseRepository(MyBatisConnectionFactory.getSqlSessionFactory());
//        List<Course> list = rdbCourseRepository.findAll();
//        List<Course> list = rdbCourseRepository.findByYear(3);
//        List<CourseDTO> courseDTOList = new ArrayList<>();
//        System.out.println(list);
//        for(Course course : list){
//            courseDTOList.add(ModelMapper.courseToDTO(course));
//        }
//        for(CourseDTO courseDTO : courseDTOList){
//            System.out.println("courseDTO = " + courseDTO);

    }
}
