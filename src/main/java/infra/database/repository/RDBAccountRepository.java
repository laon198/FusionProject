package infra.database.repository;

import domain.model.Account;
import domain.repository.AccountRepository;
import infra.database.PooledDataSource;
import infra.database.option.account.AccountOption;
import dto.AccountDTO;
import dto.ModelMapper;

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
        }catch(IndexOutOfBoundsException e){
            throw new IllegalArgumentException("id에 해당하는 결과가 없습니다.");
        }finally {
            try{
                conn.close();
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return null;
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
            List<Account> resList = getAccFrom(res);

            if(resList.size()==0){
                throw new IllegalArgumentException("해당하는 결과가 없습니다.");
            }else{
                return resList;
            }
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }finally {
            try{
                conn.close();
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
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
        }finally {
            try{
                conn.close();
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    private long add(AccountDTO accDTO){
        StringBuilder query = new StringBuilder(
                "INSERT INTO accounts_tb (account_ID, account_PW, member_PK, position) " +
                        "VALUES(?, ?, ?, ?) "
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
            pstmt.setString(4, accDTO.getPosition());

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
        }finally{
            try{
                conn.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return id;
    }

    private List<Account> getAccFrom(ResultSet res) throws SQLException {
        List<Account> list = new ArrayList<>();
        long pk;
        String id;
        String password;
        String position;
        long memberID;

        while(res.next()){
            pk = res.getLong("account_pk");
            id = res.getString("account_ID");
            password = res.getString("account_PW");
            memberID = res.getLong("member_PK");
            position = res.getString("position");

            list.add(
                    Account.builder()
                        .pk(pk)
                        .id(id)
                        .password(password)
                        .memberID(memberID)
                        .position(position)
                        .build()
            );
        }

        return list;
    }
}
