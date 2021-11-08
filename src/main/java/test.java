
import domain.model.Course;
import domain.model.Lecture;
import domain.model.Student;
import domain.repository.StudentRepository;
import infra.MyBatisConnectionFactory;
import infra.database.CourseDAO;
import infra.database.RDBCourseRepository;
import infra.database.RDBLectureRepository;
import infra.database.RDBStudentRepository;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;
import infra.option.course.CourseCodeOption;
import infra.option.student.StudentCodeOption;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        StudentRepository stdRepo = new RDBStudentRepository();
        Student std = stdRepo.findByOption(new StudentCodeOption("20180603")).get(0);
        System.out.println(std);

//        RDBLectureRepository rdbLectureRepository = new RDBLectureRepository(MyBatisConnectionFactory.getSqlSessionFactory());
//        List<Lecture> list = rdbLectureRepository.findAll();
////        System.out.println(list.size());
//        RDBCourseRepository rdbCourseRepository = new RDBCourseRepository(MyBatisConnectionFactory.getSqlSessionFactory());
//////        Course course = new Course(2,"cs0002","과기영독3","컴퓨터소프트웨어공학과",3,3);
//////        rdbCourseRepository.save(course);
//        List<Course> list = rdbCourseRepository.findByOption(new CourseCodeOption("cs0002"));
//        List<Course> list = rdbCourseRepository.findAll();


//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
////            String url = "jdbc:/**/mysql://localhost/mydb?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
//            String url = "jdbc:mysql://localhost/mydb?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
//            conn = DriverManager.getConnection(url,"root","1234");
//            String query = "SELECT * FROM members_tb";
//
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery(query);
//            while (rs.next()){
//                String id = rs.getString("member_SQ");
//                String name = rs.getString("name");
//                System.out.printf("%s     %s \n",id,name);
//            }
//        }catch(ClassNotFoundException e){
//            e.printStackTrace();
//        }catch (SQLException e){
//            System.out.println("error : "+ e);
//        }finally {
//            rs.close();
//            stmt.close();
//            conn.close();
//        }


    }
}
