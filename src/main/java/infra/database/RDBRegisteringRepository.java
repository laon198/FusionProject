package infra.database;

import domain.model.Registering;
import domain.repository.RegisteringRepository;

public class RDBRegisteringRepository implements RegisteringRepository {
    @Override
    public Registering findByID(long id) {
        return null;
    }

    @Override
    public void save(Registering registering) {

    }

    @Override
    public void remove(Registering registering) {

    }

}
