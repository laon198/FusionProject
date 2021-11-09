package infra.database;

import domain.model.Account;
import domain.model.Member;
import domain.repository.AccountRepository;
import infra.PooledDataSource;
import infra.dto.AccountDTO;
import infra.dto.ModelMapper;
import infra.dto.StudentDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RDBAccountRepository implements AccountRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Account findByID(long id) {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM accounts_tb AS a " +
                        "WHERE a.account_PK=? "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getAccFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }
    }

    @Override
    public void save(Account account) {
        AccountDTO accountDTO = ModelMapper.accountToDTO(account);
        if(accountDTO.getPk()==-1){
            add(accountDTO);
        }else{
            update(accountDTO);
        }
    }

    private void update(AccountDTO accDTO){
        StringBuilder query = new StringBuilder(
                "UPDATE accounts_tb " +
                        "SET account_ID=?, " +
                        "account_PW=? " +
                        "WHERE account_PK=? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));

            pstmt.setString(1, accDTO.getId());
            pstmt.setString(2, accDTO.getPassword());
            pstmt.setLong(3, accDTO.getPk());

            pstmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }
    }

    private void add(AccountDTO accDTO){
        StringBuilder query = new StringBuilder(
                "INSERT INTO accounts_tb (member_PK, account_ID, account_PW) " +
                        "VALUES(?, ?, ?) "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));

            pstmt.setLong(1, accDTO.getMemberID());
            pstmt.setString(2, accDTO.getId());
            pstmt.setString(3, accDTO.getPassword());

            pstmt.execute();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }

    }

    private List<Account> getAccFrom(ResultSet res) throws SQLException {
        List<Account> list = new ArrayList<>();
        long pk;
        String id;
        String password;
        long memberID;

        while(res.next()){
            pk = res.getLong("account_pk");
            id = res.getString("account_ID");
            password = res.getString("account_PW");
            memberID = res.getLong("member_PK");

            list.add(
                    Account.builder()
                        .pk(pk)
                        .id(id)
                        .password(password)
                        .memberID(memberID)
                        .build()
            );
        }

        return list;
    }
}
