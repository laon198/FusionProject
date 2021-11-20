package domain.repository;

import domain.model.Registering;
import infra.database.option.registering.RegisteringOption;

import java.util.List;

public interface RegisteringRepository {
    public void save(Registering registering);
    public void remove(Registering registering);
    public Registering findByID(long id);
    public List<Registering> findByOption(RegisteringOption... options);
}
