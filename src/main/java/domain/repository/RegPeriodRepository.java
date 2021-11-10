package domain.repository;

import domain.generic.LectureTime;
import domain.model.RegisteringPeriod;

import java.util.List;

public interface RegPeriodRepository {
    public List<RegisteringPeriod> findAll();
    public void remove(RegisteringPeriod registeringPeriod);
    public long save(RegisteringPeriod registeringPeriod);
}
