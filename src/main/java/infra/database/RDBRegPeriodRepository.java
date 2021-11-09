package infra.database;

import domain.generic.LectureTime;
import domain.model.RegisteringPeriod;
import domain.repository.RegPeriodRepository;
import infra.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RDBRegPeriodRepository implements RegPeriodRepository {
//    private final DataSource ds = PooledDataSource.getDataSource();
//
    @Override
    public List<RegisteringPeriod> findAll() {
//        StringBuilder query = new StringBuilder("SELECT * FROM registering_periods_tb ");
//
//        Connection conn = null;
//        try{
//            conn = ds.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(new String(query));
//            ResultSet res = pstmt.executeQuery();
//            return getStdFrom(res);
//        }catch(SQLException sqlException){
//            sqlException.printStackTrace();
//        }
//
        return null;
    }
}
