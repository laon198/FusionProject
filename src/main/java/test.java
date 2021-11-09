
import domain.model.Course;
import domain.model.Lecture;
import infra.MyBatisConnectionFactory;
import infra.database.CourseDAO;
import infra.database.RDBCourseRepository;
import infra.database.RDBLectureRepository;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {

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
//        }
    }
}
