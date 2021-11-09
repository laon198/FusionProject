package infra.database;

import domain.model.Registering;
import domain.repository.RegisteringRepository;
import infra.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.RegisteringDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RDBRegisteringRepository implements RegisteringRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Registering findByID(long id) {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM registerings_tb AS r " +
                        "WHERE r.registering_PK=? "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getRegFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }
    }

    @Override
    public void save(Registering registering) {
        RegisteringDTO regDTO = ModelMapper.registeringToDTO(registering);
        StringBuilder query = new StringBuilder(
                "INSERT INTO registerings_tb (student_code, lecture_PK, register_date) " +
                        "VALUES(?, ?, ?) "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(new String(query));
            stmt.setString(1, regDTO.getStudentCode());
            stmt.setLong(2, regDTO.getLectureID());
            stmt.setString(3, regDTO.getRegisteringTime());
            stmt.execute();

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            try{
                conn.rollback();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void remove(Registering registering) {
        StringBuilder query = new StringBuilder(
                "DELETE * FROM registerings_tb AS r " +
                        "WHERE r.registering_PK=? "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, registering.getID());
            pstmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    private List<Registering> getRegFrom(ResultSet res) throws SQLException {
        List<Registering> list = new ArrayList<>();
        long id;
        long lectureID;
        String studentCode;
        String registeringTime;

        while(res.next()){
            id = res.getLong("registering_PK");
            lectureID = res.getLong("lecture_PK");
            studentCode = res.getString("student_code");
            registeringTime = res.getString("register_date");

            list.add(
                    Registering.builder()
                            .id(id)
                            .lectureID(lectureID)
                            .studentCode(studentCode)
                            .registeringTime(registeringTime)
                            .build()
            );
        }

        return list;
    }
}