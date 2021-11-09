package infra.database;

import domain.generic.LectureTime;
import domain.model.Registering;
import domain.model.Student;
import domain.repository.StudentRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;
import infra.option.student.StudentOption;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
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
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getStdFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }
    }


    @Override
    public List<Student> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM students_tb AS s " +
                "JOIN members_tb AS m " +
                "ON s.member_PK = m.member_PK");
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
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
                "INSERT INTO members_tb (member_PK, name, birthday, department) " +
                        "VALUES(?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name=?, " +
                        "birthday=?, " +
                        "department=?;"
        );
        StringBuilder stdQuery = new StringBuilder(
                "INSERT INTO students_tb (member_PK, student_code, year, credit, max_credit) " +
                        "VALUES(?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "student_code=?, " +
                        "year=?, " +
                        "credit=?, " +
                        "max_credit=?;"
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setLong(1, stdDTO.getId());
            memberStmt.setString(2, stdDTO.getName());
            memberStmt.setString(3, stdDTO.getDepartment());
            memberStmt.setString(4, String.valueOf(stdDTO.getBirthDate()));
            memberStmt.setString(5, stdDTO.getName());
            memberStmt.setString(6, stdDTO.getDepartment());
            memberStmt.setString(7, String.valueOf(stdDTO.getBirthDate()));

            stdStmt.setLong(1, stdDTO.getId());
            stdStmt.setString(2, stdDTO.getStudentCode());
            stdStmt.setInt(3, stdDTO.getYear());
            stdStmt.setInt(4, stdDTO.getMaxCredit());
            stdStmt.setInt(5, stdDTO.getCredit());
            stdStmt.setString(6, stdDTO.getStudentCode());
            stdStmt.setInt(7, stdDTO.getYear());
            stdStmt.setInt(8, stdDTO.getMaxCredit());
            stdStmt.setInt(9, stdDTO.getCredit());

            memberStmt.execute();
            stdStmt.execute();
            conn.commit();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            try{
                conn.rollback();
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
        String name;
        String birthDate;
        String department;
        String studentCode;
        Set<LectureTime> timeTable = new HashSet<>();
        Set<Registering> myRegisterings = new HashSet<>();

        while(resSet.next()) {
            resID = resSet.getLong("member_SQ");
            year = resSet.getInt("year");
            name = resSet.getString("name");
            department = resSet.getString("department");
            birthDate = resSet.getString("birthDay");
            studentCode = resSet.getString("student_code");

            ResultSet lectureInfo = findLectureTime(studentCode);
            while(lectureInfo.next()){
                timeTable.add(
                        LectureTime.builder()
                        .lectureDay(lectureInfo.getString("day_of_week"))
                        .room(lectureInfo.getString("lecture_room"))
                        .startTime(lectureInfo.getInt("start_period"))
                        .endTime(lectureInfo.getInt("end_period"))
                        .build()
                );
            }

            ResultSet registering = findRegistering(studentCode);
            while(registering.next()){
                myRegisterings.add(
                        Registering.builder()
                        .id(registering.getLong("registering_SQ"))
                        .studentCode(registering.getString("student_code"))
                        .lectureID(registering.getLong("lecture_SQ"))
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
                            .timeTable(timeTable)
                            .myRegisterings(myRegisterings)
                            .build()
            );
        }

        return stdList;
    }
}
