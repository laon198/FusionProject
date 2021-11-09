package infra.database;

import domain.generic.LectureTime;
import domain.model.Professor;
import domain.repository.ProfessorRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.ProfessorDTO;

import javax.sql.DataSource;
import java.sql.*;
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
                        "ON p.member_PK = m.member_PK "+
                        "WHERE p.member_PK = ? "
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
                        "ON p.member_PK = m.member_PK");
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
        ProfessorDTO profDTO = ModelMapper.professorToDTO(professor);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (member_PK, name, birthday, department) " +
                        "VALUES(?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name=?, " +
                        "birthday=?, " +
                        "department=?;"
        );
        StringBuilder profQuery = new StringBuilder(
                "INSERT INTO professors_tb (member_PK, professor_code) " +
                        "VALUES(?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "professor_code=? ; "
        );

        Connection conn = null;
        Savepoint point1 = null;
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

            point1 = conn.setSavepoint("point1");
            boolean b = memberStmt.execute();
            conn.commit();
            boolean a = profStmt.execute();
            //TODO : Unique컬럼에 대한 예외는 안뜨고 삽입은 안됨
            conn.commit();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            if(conn!=null){
                try{
                    conn.rollback(point1);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    private ResultSet findLectureInfo(String profCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM lectures_tb AS l " +
                        "JOIN lecture_times_tb AS t " +
                        "ON l.lecture_PK = t.lecture_PK "+
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
                            .timeTable(timeTable)
                            .build()
            );
        }

        return profList;
    }

}
