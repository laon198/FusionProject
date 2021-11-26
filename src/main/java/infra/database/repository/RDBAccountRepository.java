package infra.database.repository;

import domain.model.Account;
import domain.repository.AccountRepository;
import infra.database.PooledDataSource;
import infra.database.option.account.AccountOption;
import infra.dto.AccountDTO;
import infra.dto.ModelMapper;

import javax.sql.DataSource;
import java.sql.*;
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
    public List<Account> findByOption(AccountOption... options){
        String and = " AND ";
        String where = " WHERE ";
        StringBuilder query = new StringBuilder(
                "SELECT * FROM accounts_tb AS a "
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

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getAccFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public long save(Account account) {
        AccountDTO accountDTO = ModelMapper.accountToDTO(account);
        if(accountDTO.getPk()==-1){
            return add(accountDTO);
        }else{
            update(accountDTO);
            return accountDTO.getPk();
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

    private long add(AccountDTO accDTO){
        StringBuilder query = new StringBuilder(
                "INSERT INTO accounts_tb (account_ID, account_PW, member_PK) " +
                        "VALUES(?, ?, ?) "
        );

        Connection conn = null;
        long id = -1;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    new String(query),
                    Statement.RETURN_GENERATED_KEYS
            );

            pstmt.setString(1, accDTO.getId());
            pstmt.setString(2, accDTO.getPassword());
            pstmt.setLong(3, accDTO.getMemberID());

            pstmt.executeUpdate();
            ResultSet res = pstmt.getGeneratedKeys();
            while(res.next()){
                id = res.getLong(1);
            }

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
//            try{
//                conn.rollback();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
        }
        return id;
    }

    private List<Account> getAccFrom(ResultSet res) throws SQLException {
        List<Account> list = new ArrayList<>();
        long pk;
        String id;
        String password;
        String position;

        while(res.next()){
            pk = res.getLong("account_pk");
            id = res.getString("account_ID");
            password = res.getString("account_PW");
            position = res.getString("position");

            list.add(
                    Account.builder()
                        .pk(pk)
                        .id(id)
                        .password(password)
                        .position(position)
                        .build()
            );
        }

        return list;
    }
}
