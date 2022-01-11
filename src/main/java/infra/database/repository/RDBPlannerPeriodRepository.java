package infra.database.repository;

import domain.model.Period;
import domain.repository.PlannerPeriodRepository;
import infra.database.PooledDataSource;
import dto.ModelMapper;
import dto.PeriodDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class RDBPlannerPeriodRepository implements PlannerPeriodRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Period find() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM planner_input_periods_tb"
        );
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getPeriodFrom(res);
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
    public void save(Period period) {
        PeriodDTO dto = ModelMapper.periodToDTO(period);
        StringBuilder query = new StringBuilder(
                "INSERT INTO planner_input_periods_tb (start_period, end_period) " +
                        "VALUES(?, ?) "
        );

        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(true);
            stmt = conn.prepareStatement(new String(query));

            stmt.setTimestamp(1, Timestamp.valueOf(dto.getBeginTime() ));
            stmt.setTimestamp(2, Timestamp.valueOf(dto.getEndTime()));

            stmt.executeUpdate();
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
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete() {
        StringBuilder query = new StringBuilder(
                "DELETE FROM planner_input_periods_tb "
        );

        Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(true);
            stmt = conn.prepareStatement(new String(query));

            stmt.executeUpdate();
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
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private Period getPeriodFrom(ResultSet res) throws SQLException, IllegalArgumentException {
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;

        while(res.next()){
            beginTime = res.getTimestamp("start_period").toLocalDateTime();
            endTime = res.getTimestamp("end_period").toLocalDateTime();
        }

        if(beginTime==null || endTime==null){
            throw new IllegalArgumentException("해당하는 결과가 없습니다.");
        }

        return new Period(
                beginTime, endTime
        );
    }
}
