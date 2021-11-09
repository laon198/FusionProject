package infra.database;

import domain.generic.LectureTime;
import domain.model.Professor;
import domain.repository.ProfessorRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.ProfessorDTO;
import infra.dto.StudentDTO;

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
    public long save(Professor professor) {
        if(professor.getID()==-1){
            return add(professor);
        }else{
            update(professor);
            return professor.getID();
        }
    }

    private void update(Professor prof){
         ProfessorDTO profDTO = ModelMapper.professorToDTO(prof);
        StringBuilder memberQuery = new StringBuilder(
                "UPDATE members_tb " +
                        "SET name=?, " +
                        "birthday=?, " +
                        "department=? " +
                        "WHERE member_PK=? "
        );
        StringBuilder profQuery = new StringBuilder(
                "UPDATE professors_tb " +
                        "SET professor_code=?," +
                        "phone_number=? " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement stdStmt = conn.prepareStatement(new String(profQuery));

            memberStmt.setString(1, profDTO.getName());
            memberStmt.setString(2, profDTO.getBirthDate());
            memberStmt.setString(3, profDTO.getDepartment());
            memberStmt.setLong(4, profDTO.getId());

            stdStmt.setString(1, profDTO.getProfessorCode());
            stdStmt.setString(2, profDTO.getTelePhone());
            stdStmt.setLong(3, profDTO.getId());

            memberStmt.execute();
            stdStmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }

    }

    private long add(Professor prof){
        ProfessorDTO profDTO = ModelMapper.professorToDTO(prof);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (name, birthday, department) " +
                        "VALUES(?, ?, ?) "
        );
        StringBuilder profQuery = new StringBuilder(
                "INSERT INTO professors_tb (member_PK, professor_code, phone_number) " +
                        "VALUES(?, ?, ?) "
        );

        Connection conn = null;
        long id=-1;
        try{
            conn = ds.getConnection();
            PreparedStatement memberStmt = conn.prepareStatement(
                    new String(memberQuery),
                    Statement.RETURN_GENERATED_KEYS
                    );
            PreparedStatement stdStmt = conn.prepareStatement(new String(profQuery));

            memberStmt.setString(1, profDTO.getName());
            memberStmt.setString(2, profDTO.getBirthDate());
            memberStmt.setString(3, profDTO.getDepartment());

            memberStmt.execute();
            ResultSet res = memberStmt.getGeneratedKeys();
            while(res.next()){
                id = res.getLong(1);
            }

            stdStmt.setLong(1, id);
            stdStmt.setString(2, profDTO.getProfessorCode());
            stdStmt.setString(3, profDTO.getTelePhone());

            stdStmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }finally {
            return id;
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
        String telePhone;
        Set<LectureTime> timeTable;

        while(resSet.next()) {
            resID = resSet.getLong("member_PK");
            professor_code = resSet.getString("professor_code");
            name = resSet.getString("name");
            birthDay = resSet.getString("birthDay");
            department = resSet.getString("department");
            telePhone = resSet.getString("phone_number");
            timeTable = new HashSet<>();

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
                            .telePhone(telePhone)
                            .timeTable(timeTable)
                            .build()
            );
        }

        return profList;
    }

}
