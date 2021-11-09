package domain.model;

import domain.generic.Period;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class RegisteringPeriod {
    private Period period;
    private Set<Student.Year> allowedYearSet;

    public RegisteringPeriod(Period period, Set<Student.Year> allowedYears){
        this.period = period;
        allowedYearSet = allowedYears;
    }

    //TODO : method naming??
    public boolean isSatisfiedBy(Student std){
        LocalDateTime nowTime = LocalDateTime.now();

        if(!isAllowedYear(std.getYear())){
            return false;
        }

        if(!period.isInPeriod(nowTime)){
            return false;
        }

        return true;
    }

    private boolean isAllowedYear(Student.Year year){
        for(Student.Year allowedYear : allowedYearSet){
            if(allowedYear==year){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteringPeriod that = (RegisteringPeriod) o;
        return Objects.equals(period, that.period) && Objects.equals(allowedYearSet, that.allowedYearSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, allowedYearSet);
    }
}
