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
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_SQ = m.member_SQ "+
                "WHERE m.member_SQ = ? "
        );
        try{
            PreparedStatement pstmt = getPreparedStmt(new String(query));
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
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_SQ = m.member_SQ");
        try{
            PreparedStatement pstmt = getPreparedStmt(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Student student) {
    }

    private List<Student> getStdFrom(ResultSet resSet) throws SQLException {
        List<Student> stdList = new ArrayList<>();
        long resID = 0;
        int year = 0;
        String name;
        String birthDate;

        while(resSet.next()) {
            resID = resSet.getLong("member_SQ");
            year = resSet.getInt("year");
            name = resSet.getString("name");

            stdList.add(
                    Student.builder()
                            .id(resID)
                            .year(year)
                            .name(name)
                            .build()
            );
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

}
