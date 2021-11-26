package infra.database.repository;

import domain.model.LectureTime;
import domain.model.Registering;
import domain.model.Student;
import domain.repository.StudentRepository;
import infra.database.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;
import infra.database.option.student.StudentOption;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class RDBStudentRepository implements StudentRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public List<Student> findByOption(StudentOption... options) {
        String and = " AND ";
        String where = " WHERE ";
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                        "JOIN members_tb AS m " +
                        "ON s.member_PK = m.member_PK "
        );

        for(int i=0; i<options.length; i++){
            if(i==0){
                query.append(where);
            }

            query.append(options[i].getQuery());

            if(i!=options.length-1){
                query.append(and);
            }
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                pstmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Student findByID(long id){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_PK = m.member_PK "+
                "WHERE m.member_PK = ? "
        );
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }finally {
            try{
                pstmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public List<Student> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_PK = m.member_PK");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                pstmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return null;
    }
    @Override
    public long save(Student student) {
        if(student.getID()==-1){
            return add(student);
        }else{
            update(student);
            return student.getID();
        }
    }

    private long add(Student student) {
        StudentDTO stdDTO = ModelMapper.studentToDTO(student);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (name, birthday, department) " +
                        "VALUES(?, ?, ?) "
        );
        StringBuilder stdQuery = new StringBuilder(
                "INSERT INTO students_tb (member_PK, student_code, year, credit, max_credit) " +
                        "VALUES(?, ?, ?, ?, ?) "
        );

        Connection conn = null;
        PreparedStatement memberStmt = null;
        PreparedStatement stdStmt = null;
        long id=-1;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(true);
            memberStmt = conn.prepareStatement(
                    new String(memberQuery),
                    Statement.RETURN_GENERATED_KEYS);
            stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setString(1, stdDTO.getName());
            memberStmt.setString(2, stdDTO.getBirthDate());
            memberStmt.setString(3, stdDTO.getDepartment());

            memberStmt.executeUpdate();
            ResultSet res = memberStmt.getGeneratedKeys();

            while(res.next()){
                 id = res.getLong(1);
            }

            stdStmt.setLong(1, id);
            stdStmt.setString(2, stdDTO.getStudentCode());
            stdStmt.setInt(3, stdDTO.getYear());
            stdStmt.setInt(4, stdDTO.getCredit());
            stdStmt.setInt(5, stdDTO.getMaxCredit());

            stdStmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }finally {
            try{
                memberStmt.close();
                stdStmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return id;
    }

    private void update(Student student) {
        StudentDTO stdDTO = ModelMapper.studentToDTO(student);
        StringBuilder memberQuery = new StringBuilder(
                "UPDATE members_tb " +
                        "SET name=?, " +
                        "birthday=?, " +
                        "department=? " +
                        "WHERE member_PK=? "
        );
        StringBuilder stdQuery = new StringBuilder(
                "UPDATE students_tb " +
                        "SET student_code=?, " +
                        "year=?, " +
                        "max_credit=?, " +
                        "credit=? " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        PreparedStatement memberStmt = null;
        PreparedStatement stdStmt = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(true);
            memberStmt = conn.prepareStatement(new String(memberQuery));
            stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setString(1, stdDTO.getName());
            memberStmt.setString(2, stdDTO.getBirthDate());
            memberStmt.setString(3, stdDTO.getDepartment());
            memberStmt.setLong(4, stdDTO.getId());

            stdStmt.setString(1, stdDTO.getStudentCode());
            stdStmt.setInt(2, stdDTO.getYear());
            stdStmt.setInt(3, stdDTO.getMaxCredit());
            stdStmt.setInt(4, stdDTO.getCredit());
            stdStmt.setLong(5, stdDTO.getId());

            memberStmt.executeUpdate();
            stdStmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }finally {
            try{
                memberStmt.close();
                stdStmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private ResultSet findLectureTime(String stdCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM registerings_tb AS r " +
                        "JOIN lecture_times_tb AS t " +
                        "ON r.lecture_PK = t.lecture_PK "+
                        "WHERE r.student_code = ? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setString(1, stdCode);
            return pstmt.executeQuery();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    private ResultSet findRegistering(String stdCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM registerings_tb AS r " +
                        "WHERE r.student_code = ? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setString(1, stdCode);
            return pstmt.executeQuery();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    private List<Student> getStdFrom(ResultSet resSet) throws SQLException {
        List<Student> stdList = new ArrayList<>();
        long resID = 0;
        int year = 0;
        int credit = 0;
        int maxCredit = 0;
        String name;
        String birthDate;
        String department;
        String studentCode;
        Set<LectureTime> timeTable;
        Set<Registering> myRegisterings;

        while(resSet.next()) {
            resID = resSet.getLong("member_PK");
            year = resSet.getInt("year");
            name = resSet.getString("name");
            department = resSet.getString("department");
            birthDate = resSet.getString("birthDay");
            studentCode = resSet.getString("student_code");
            credit = resSet.getInt("credit");
            maxCredit = resSet.getInt("max_credit");
            timeTable = new HashSet<>();
            myRegisterings = new HashSet<>();

            ResultSet lectureInfo = findLectureTime(studentCode);
            while(lectureInfo.next()){
                timeTable.add(
                        LectureTime.builder()
                        .lectureDay(lectureInfo.getString("day_of_week"))
                        .room(lectureInfo.getString("lecture_room"))
                        .startTime(lectureInfo.getInt("start_period"))
                        .endTime(lectureInfo.getInt("end_period"))
                        .lectureName(lectureInfo.getString("lectureName"))
                        .build()
                );
            }

            ResultSet registering = findRegistering(studentCode);
            while(registering.next()){
                myRegisterings.add(
                        Registering.builder()
                        .id(registering.getLong("registering_PK"))
                        .studentCode(registering.getString("student_code"))
                        .lectureID(registering.getLong("lecture_PK"))
                        .registeringTime(registering.getString("register_date"))
                        .build()
                );
            }

            stdList.add(
                    Student.builder()
                            .id(resID)
                            .year(year)
                            .name(name)
                            .birthDate(birthDate)
                            .department(department)
                            .studentCode(studentCode)
                            .credit(credit)
                            .maxCredit(maxCredit)
                            .timeTable(timeTable)
                            .myRegisterings(myRegisterings)
                            .build()
            );
        }

        return stdList;
    }

    @Override
    public void remove(Student student){
        StudentDTO stdDTO = ModelMapper.studentToDTO(student);
        StringBuilder memberQuery = new StringBuilder(
                "DELETE FROM members_tb " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        PreparedStatement memberStmt = null;
        PreparedStatement stdStmt = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(true);
            memberStmt = conn.prepareStatement(new String(memberQuery));

            memberStmt.setLong(1, stdDTO.getId());

            memberStmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }finally {
            try{
                memberStmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
