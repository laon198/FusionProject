package domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class RegisteringPeriod {
    private long id;
    private Period period;
    private Year allowedYear;

    public static class Builder{
        private long id;
        private Period period;
        private Year allowedYear;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder period(Period value){
            period = value;
            return this;
        }

        public Builder allowedYear(Year value){
            allowedYear = value;
            return this;
        }

        public RegisteringPeriod build(){
            return new RegisteringPeriod(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private RegisteringPeriod(Builder builder){
        id = builder.id;
        period = builder.period;
        allowedYear = builder.allowedYear;
    }

    //해당학년이 수강신청가능한 기간인지 확인
    public boolean isSatisfiedBy(Student std, Course course){
        LocalDateTime nowTime = LocalDateTime.now();

        if(!isAllowedYear(std.getYear(), course.getYear())){
            return false;
        }

        if(!period.isInPeriod(nowTime)){
            return false;
        }

        return true;
    }

    //해당학년이 수강신청가능한 학년인지 확인
    private boolean isAllowedYear(Year year, Year courseYear){
        if(allowedYear==year && allowedYear==courseYear){
            return true;
        }

        return false;
    }

    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteringPeriod that = (RegisteringPeriod) o;
        return Objects.equals(period, that.period) && allowedYear == that.allowedYear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, allowedYear);
    }
}
