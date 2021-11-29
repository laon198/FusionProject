package infra.database.repository;

import domain.model.LectureTime;
import domain.model.Professor;
import domain.repository.ProfessorRepository;
import infra.database.PooledDataSource;
import infra.database.option.professor.ProfessorOption;
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
        Professor prof = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            res = pstmt.executeQuery();
            prof = getProfFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return prof;
    }

    @Override
    public List<Professor> findByOption(ProfessorOption... options) {
        String and = " AND ";
        String where = " WHERE ";
        StringBuilder query = new StringBuilder(
                "SELECT * FROM professors_tb AS p " +
                        "JOIN members_tb AS m " +
                        "ON p.member_PK = m.member_PK "
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

        Professor prof = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            res = pstmt.executeQuery();
            return getProfFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Professor> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM professors_tb AS p " +
                        "JOIN members_tb AS m " +
                        "ON p.member_PK = m.member_PK");
        List<Professor> list = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            res = pstmt.executeQuery();
            list = getProfFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return list;
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
        PreparedStatement memberStmt = null;
        PreparedStatement stdStmt = null;
        try{
            conn = ds.getConnection();
            memberStmt = conn.prepareStatement(new String(memberQuery));
            stdStmt = conn.prepareStatement(new String(profQuery));

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
        }finally {
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
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
        PreparedStatement memberStmt = null;
        PreparedStatement stdStmt = null;
        long id=-1;
        try{
            conn = ds.getConnection();
            memberStmt = conn.prepareStatement(
                    new String(memberQuery),
                    Statement.RETURN_GENERATED_KEYS
                    );
            stdStmt = conn.prepareStatement(new String(profQuery));

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
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return id;
    }

    private ResultSet findLectureInfo(String profCode){
        StringBuilder query = new StringBuilder(
                "SELECT * FROM lectures_tb AS l " +
                        "JOIN lecture_times_tb AS t " +
                        "ON l.lecture_PK = t.lecture_PK "+
                        "WHERE l.professor_code = ? "
        );

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            pstmt.setString(1, profCode);
            res = pstmt.executeQuery();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return res;
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
                                .lectureName(lectureInfo.getString("lecture_name"))
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

    @Override
    public void remove(Professor prof){
        ProfessorDTO profDTO = ModelMapper.professorToDTO(prof);
        StringBuilder memberQuery = new StringBuilder(
                "DELETE FROM members_tb " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        PreparedStatement memberStmt = null;
        try{
            conn = ds.getConnection();
            memberStmt = conn.prepareStatement(new String(memberQuery));

            memberStmt.setLong(1, profDTO.getId());

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
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
}
