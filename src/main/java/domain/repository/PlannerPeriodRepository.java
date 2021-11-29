package domain.repository;

import domain.model.Period;

public interface PlannerPeriodRepository {
    Period find();
    void save(Period period);
    void delete();
}
