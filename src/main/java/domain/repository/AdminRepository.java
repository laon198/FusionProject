package domain.repository;

import domain.model.Admin;

import java.util.List;

public interface AdminRepository {
    public Admin findByID(long id);
    public List<Admin> findAll();
    public long save(Admin admin);
    public void remove(Admin admin);
}
