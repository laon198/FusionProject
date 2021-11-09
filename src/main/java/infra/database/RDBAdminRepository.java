package infra.database;

import domain.model.Admin;
import domain.repository.AdminRepository;
import infra.PooledDataSource;
import infra.dto.AdminDTO;
import infra.dto.ModelMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RDBAdminRepository implements AdminRepository {
    private final DataSource ds = PooledDataSource.getDataSource();

    @Override
    public Admin findByID(long id) {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM admins_tb AS a " +
                        "JOIN members_tb AS m " +
                        "ON a.admins_PK = m.member_PK "+
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
            throw new IllegalArgumentException("잘못된 id값입니다.");
        }
    }

    @Override
    public List<Admin> findAll() {
        StringBuilder query = new StringBuilder(
                "SELECT * FROM admins_tb AS a " +
                        "JOIN members_tb AS m " +
                        "ON a.admins_PK = m.member_PK "
        );
        Connection conn = null;
        try{
            conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(new String(query));
            ResultSet res = pstmt.executeQuery();
            return getAdminFrom(res);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Admin admin) {
        AdminDTO dto = ModelMapper.adminToDTO(admin);

        StringBuilder memberQuery = new StringBuilder(
                "INSERT INTO members_tb (member_PK, name, birthday, department) " +
                        "VALUES(?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name=?, " +
                        "birthday=?, " +
                        "department=?;"
        );
        StringBuilder stdQuery = new StringBuilder(
                "INSERT INTO admin (member_PK, admin_code) " +
                        "VALUES(?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "admin_code=? "
        );

        Connection conn = null;
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement memberStmt = conn.prepareStatement(new String(memberQuery));
            PreparedStatement stdStmt = conn.prepareStatement(new String(stdQuery));

            memberStmt.setLong(1, dto.getId());
            memberStmt.setString(2, dto.getName());
            memberStmt.setString(3, dto.getDepartment());
            memberStmt.setString(4, String.valueOf(dto.getBirthDate()));
            memberStmt.setString(5, dto.getName());
            memberStmt.setString(6, dto.getDepartment());
            memberStmt.setString(7, dto.getBirthDate());

            stdStmt.setLong(1, dto.getId());
            stdStmt.setString(2, dto.getAdminCode());

            memberStmt.execute();
            stdStmt.execute();
            conn.commit();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
            try{
                conn.rollback();
            }catch (SQLException e){
                e.printStackTrace();
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
            id = res.getLong("member_SQ");
            name = res.getString("name");
            department = res.getString("department");
            birthDay = res.getString("birthDay");
            adminCode = res.getString("admin_code");

            list.add(
                    Admin.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .birthDate(birthDay)
                    .adminCode(adminCode)
                    .build()
            );
        }

        return list;
    }
}
