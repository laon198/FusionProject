package infra.database;

import domain.generic.LectureTime;
import domain.model.Professor;
import domain.model.Student;
import domain.repository.ProfessorRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.ProfessorDTO;
import infra.dto.StudentDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RDBProfessorRepository implements ProfessorRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Professor findByID(long id) {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM professors_tb AS p " +
                        "JOIN members_tb AS m " +
                        "ON p.member_SQ = m.member_SQ "+
                        "WHERE p.member_SQ = ? "
        );
        Connection conn = null;

        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();

            return getProfFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }

    }

    @Override
    public List<Professor> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM professors_tb AS p " +
                        "JOIN members_tb AS m " +
                        "ON p.member_SQ = m.member_SQ");
        try{
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getProfFrom(res);

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Professor professor) {
        ProfessorDTO profDTO = ModelMapper.ProfessorToDTO(professor);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (member_SQ, name, birthday, department) " +
                        "VALUES(?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name=?, " +
                        "birthday=?, " +
                        "department=?;"
        );
        StringBuilder profQuery = new StringBuilder(
                "INSERT INTO professors_tb (member_SQ, professor_code) " +
                        "VALUES(?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "professor_code=? ; "
        );

        Connection conn = null;

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement profStmt = conn.prepareStatement(new String(profQuery));

            memberStmt.setLong(1, profDTO.getId());
            memberStmt.setString(2, profDTO.getName());
            memberStmt.setString(3, profDTO.getBirthDate());
            memberStmt.setString(4, profDTO.getDepartment());
            memberStmt.setString(5, profDTO.getName());
            memberStmt.setString(6, profDTO.getBirthDate());
            memberStmt.setString(7, profDTO.getDepartment());

            profStmt.setLong(1, profDTO.getId());
            profStmt.setString(2, profDTO.getProfessorCode());
            profStmt.setString(3, profDTO.getProfessorCode());


            memberStmt.execute();
            profStmt.execute();

            conn.commit();
        }catch(SQLException sqlException){
            try{
                conn.rollback();
            }catch (SQLException e){
                e.printStackTrace();
            }
            sqlException.printStackTrace();
        }

    }

    private ResultSet findLectureInfo(String profCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM lectures_tb AS l " +
                        "JOIN lecture_times_tb AS t " +
                        "ON l.lecture_SQ = t.lecture_SQ "+
                        "WHERE l.professor_code = ? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setString(1, profCode);
            return pstmt.executeQuery();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    private List<Professor> getProfFrom(ResultSet resSet) throws SQLException {
        List<Professor> profList = new ArrayList<>();
        long resID = 0;
        String professor_code;
        String name;
        String birthDay;
        String department;
        Set<LectureTime> timeTable = new HashSet<>();

        while(resSet.next()) {
            resID = resSet.getLong("member_SQ");
            professor_code = resSet.getString("professor_code");
            name = resSet.getString("name");
            birthDay = resSet.getString("birthDay");
            department = resSet.getString("department");

            ResultSet lectureInfo = findLectureInfo(professor_code);
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

            profList.add(
                    Professor.builder()
                            .id(resID)
                            .name(name)
                            .birthDate(birthDay)
                            .department(department)
                            .professorCode(professor_code)
                            .build()
            );
        }

        return profList;
    }

}