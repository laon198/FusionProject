package infra.database;

import domain.generic.Period;
import domain.model.RegisteringPeriod;
import domain.repository.RegPeriodRepository;
import infra.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        return null;
    }

    private List<RegisteringPeriod> getRegPeriodFrom(ResultSet res) throws SQLException {
        List<RegisteringPeriod> list = new ArrayList<>();

        while(res.next()){
            LocalDateTime beginTime = res.getTimestamp("start_period").toLocalDateTime();
            LocalDateTime endTime = res.getTimestamp("end_period").toLocalDateTime();
            int allowedYear = res.getInt("target_year");

            list.add(
                    RegisteringPeriod.builder()
                        .period(new Period(beginTime, endTime))
                        .allowedYear(allowedYear)
                        .build()
            );
        }

        return list;
    }
}
