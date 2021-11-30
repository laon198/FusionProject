package infra.database.repository;

import domain.model.Period;
import domain.model.RegisteringPeriod;
import domain.repository.RegPeriodRepository;
import infra.database.PooledDataSource;
import infra.dto.ModelMapper;
import infra.dto.RegisteringPeriodDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RDBRegPeriodRepository implements RegPeriodRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public List<RegisteringPeriod> findAll() {
        StringBuilder query = new StringBuilder("SELECT * FROM registering_periods_tb ");

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getRegPeriodFrom(res);
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

        return new ArrayList<>();
    }

    @Override
    public long save(RegisteringPeriod registeringPeriod) {
        RegisteringPeriodDTO regPeriodDTO = ModelMapper.regPeriodToDTO(registeringPeriod);
        StringBuilder query = new StringBuilder()
                .append("INSERT INTO registering_periods_tb (start_period, end_period, target_year) ")
                .append("VALUES(?, ?, ?) ");

        Connection conn = null;
        PreparedStatement stmt = null;
        long id=-1;
        try{
            conn = ds.getConnection();
            stmt = conn.prepareStatement(new String(query), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1, Timestamp.valueOf(regPeriodDTO.getPeriodDTO().getBeginTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(regPeriodDTO.getPeriodDTO().getEndTime()));
            stmt.setInt(3, regPeriodDTO.getAllowedYear());

            stmt.executeUpdate();
            ResultSet res = stmt.getGeneratedKeys();
            while(res.next()){
                id = res.getLong(1);
            }

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            try{
                stmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return id;
    }

    @Override
    public void remove(RegisteringPeriod registeringPeriod) {
        StringBuilder query = new StringBuilder()
                .append("DELETE FROM registering_periods_tb AS r ")
                .append("WHERE r.registering_period_PK=? ");

        Connection conn = null;
        PreparedStatement pstmt;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, registeringPeriod.getID());
            pstmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                conn.close();
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }


    private List<RegisteringPeriod> getRegPeriodFrom(ResultSet res) throws SQLException {
        List<RegisteringPeriod> list = new ArrayList<>();

        while(res.next()){
            long id = res.getLong("registering_period_PK");
            LocalDateTime beginTime = res.getTimestamp("start_period").toLocalDateTime();
            LocalDateTime endTime = res.getTimestamp("end_period").toLocalDateTime();
            int allowedYear = res.getInt("target_year");

            list.add(
                    RegisteringPeriod.builder()
                        .id(id)
                        .period(new Period(beginTime, endTime))
                        .allowedYear(allowedYear)
                        .build()
            );
        }

        return list;
    }
}
