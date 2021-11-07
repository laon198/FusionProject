package infra.database;

import domain.model.Student;
import domain.repository.StudentRepository;
import infra.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RDBStudentRepository implements StudentRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Student findByID(long id){
        String query = "SELECT * FROM students_tb WHERE student_ID=?";
        try{
            PreparedStatement pstmt = getPreparedStmt(query);
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Student> findAll() {
        String query = "SELECT * FROM students_tb";
        try{
            PreparedStatement pstmt = getPreparedStmt(query);
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    private List<Student> getStdFrom(ResultSet resSet) throws SQLException {
        List<Student> stdList = new ArrayList<>();
        long resID = 0;
        int grade = 0;

        while(resSet.next()) {
            resID = resSet.getLong("student_ID");
            grade = resSet.getInt("grade");

            if (grade == 1) {
                stdList.add(
                        new Student(resID, Student.Year.FRESHMAN)
                );
            } else if (grade == 2) {
                stdList.add(
                        new Student(resID, Student.Year.SOPHOMORE)
                );
            } else if (grade == 3) {
                stdList.add(
                        new Student(resID, Student.Year.JUNIOR)
                );
            } else {
                stdList.add(
                        new Student(resID, Student.Year.SENIOR)
                );
            }
        }

        return stdList;
    }


    //TODO : prepareStmt받아오는거 따로 클래스 뺄까?
    private PreparedStatement getPreparedStmt(String query) throws SQLException {
        PreparedStatement pstmt = null;
        Connection conn =  ds.getConnection();
        pstmt = conn.prepareStatement(query);
        return pstmt;
    }

    @Override
    public void save(Student student) {
    }
}
