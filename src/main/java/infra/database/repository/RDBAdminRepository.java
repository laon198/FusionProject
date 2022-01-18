package infra.database.repository;

import domain.model.Admin;
import domain.repository.AdminRepository;
import infra.database.PooledDataSource;
import dto.AdminDTO;
import dto.ModelMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RDBAdminRepository implements AdminRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Admin findByID(long id) {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM admins_tb AS a " +
                        "JOIN members_tb AS m " +
                        "ON a.member_PK = m.member_PK "+
                        "WHERE m.member_PK = ? "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            pstmt.setLong(1, id);
            ResultSet res = pstmt.executeQuery();
            return getAdminFrom(res).get(0);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }catch(IndexOutOfBoundsException e){
            throw new IllegalArgumentException("해당하는 결과가 없습니다.");
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
    public List<Admin> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM admins_tb AS a " +
                        "JOIN members_tb AS m " +
                        "ON a.member_PK = m.member_PK "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            List<Admin> resList = getAdminFrom(res);
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
    public long save(Admin admin) {
        if(admin.getID()==-1){
            return add(admin);
        }else{
            update(admin);
            return admin.getID();
        }
    }

    private long add(Admin admin){
        AdminDTO adminDTO = ModelMapper.adminToDTO(admin);
        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (name, birthday, department) " +
                        "VALUES(?, ?, ?) "
        );
        StringBuilder stdQuery = new StringBuilder(
                "INSERT INTO admins_tb (member_PK, admin_code) " +
                        "VALUES(?, ?) "
        );

        Connection conn = null;
        long id=-1;
        try{
            conn = ds.getConnection();
            PreparedStatement memberStmt = conn.prepareStatement(
                    new String(memberQuery),
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setString(1, adminDTO.getName());
            memberStmt.setString(2, adminDTO.getBirthDate());
            memberStmt.setString(3, adminDTO.getDepartment());

            memberStmt.execute();
            ResultSet res = memberStmt.getGeneratedKeys();
            while(res.next()){
                id = res.getLong(1);
            }

            stdStmt.setLong(1, id);
            stdStmt.setString(2, adminDTO.getAdminCode());

            stdStmt.execute();
        }catch(SQLIntegrityConstraintViolationException e){
            throw new IllegalArgumentException("중복되는 교직원코드입니다.");
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

        return id;
    }

    private void update(Admin admin){
        AdminDTO adminDTO = ModelMapper.adminToDTO(admin);
        StringBuilder memberQuery = new StringBuilder(
                "UPDATE members_tb " +
                        "SET name=?, " +
                        "birthday=?, " +
                        "department=? " +
                        "WHERE member_PK=? "
        );
        StringBuilder stdQuery = new StringBuilder(
                "UPDATE admins_tb " +
                        "SET admin_code=? " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setString(1, adminDTO.getName());
            memberStmt.setString(2, adminDTO.getBirthDate());
            memberStmt.setString(3, adminDTO.getDepartment());
            memberStmt.setLong(4, adminDTO.getId());

            stdStmt.setString(1, adminDTO.getAdminCode());
            stdStmt.setLong(2, adminDTO.getId());

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
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

    }

    private List<Admin> getAdminFrom(ResultSet res) throws SQLException {
        List<Admin> list = new ArrayList<>();
        long id;
        String name;
        String birthDay;
        String department;
        String adminCode;

        while(res.next()){
            id = res.getLong("member_PK");
            name = res.getString("name");
            department = res.getString("department");
            birthDay = res.getString("birthDay");
            adminCode = res.getString("admin_code");

            list.add(
                    Admin.builder(name, department, birthDay, adminCode)
                    .id(id)
                    .build()
            );
        }

        return list;
    }

    public void remove(Admin admin){
        AdminDTO adminDTO = ModelMapper.adminToDTO(admin);
        StringBuilder memberQuery = new StringBuilder(
                "DELETE FROM members_tb " +
                        "WHERE member_PK=? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));

            memberStmt.setLong(1, adminDTO.getId());

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
            }catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

    }
}
