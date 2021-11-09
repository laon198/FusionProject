package domain.repository;

import domain.model.Registering;

public interface RegisteringRepository {
    public void save(Registering registering);
    public void remove(Registering registering);
    public Registering findByID(long id);
}
