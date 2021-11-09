package infra.database;

import domain.generic.LectureTime;
import domain.model.RegisteringPeriod;
import domain.repository.RegPeriodRepository;

import java.util.List;

public class RDBRegPeriodRepository implements RegPeriodRepository {
    @Override
    public List<RegisteringPeriod> findAll() {
        return null;
    }
}
