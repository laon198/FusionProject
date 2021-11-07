package infra.database;

import domain.model.Student;
import domain.repository.StudentRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;

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
        StudentDTO stdDTO = ModelMapper.studentToDTO(student);
        StringBuilder memberQuery = new StringBuilder(
                "UPDATE members_tb " +
                "SET name=?, " +
                "department=?, " +
                "birthday=? " +
                "WHERE member_SQ=?;"
        );
        StringBuilder stdQuery = new StringBuilder(
                "UPDATE students_tb " +
                "SET student_code=?, " +
                "year=?, " +
                "max_credit=?, " +
                "credit=? " +
                "WHERE member_SQ=?;"
        );

        try{
            PreparedStatement memberStmt = getPreparedStmt(new String(memberQuery));
            PreparedStatement stdStmt = getPreparedStmt(new String(stdQuery));

            memberStmt.setString(1, stdDTO.getName());
            memberStmt.setString(2, stdDTO.getDepartment());
            memberStmt.setString(3, String.valueOf(stdDTO.getBirthDate()));
            memberStmt.setLong(4, stdDTO.getId());

            stdStmt.setString(1, stdDTO.getStudentCode());
            stdStmt.setInt(2, stdDTO.getYear());
            stdStmt.setInt(3, stdDTO.getMaxCredit());
            stdStmt.setInt(4, stdDTO.getCredit());
            stdStmt.setLong(5, stdDTO.getId());

            memberStmt.executeUpdate();
            stdStmt.executeUpdate();

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
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
