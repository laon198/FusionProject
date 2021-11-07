import domain.model.Course;
import infra.MyBatisConnectionFactory;
import infra.database.CourseDAO;

import java.sql.*;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {

        CourseDAO courseDAO = new CourseDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        List<Course> courses = courseDAO.ReadAll();
        System.out.println(courses.size());


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
