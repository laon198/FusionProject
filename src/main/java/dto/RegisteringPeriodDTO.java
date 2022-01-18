package dto;

import domain.model.Year;

public class RegisteringPeriodDTO {
    private long id;
    private PeriodDTO period;
    private Year allowedYear;

    public static class Builder{
        private long id;
        private PeriodDTO period;
        private Year allowedYear;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder period(PeriodDTO value){
            period = value;
            return this;
        }

        public Builder allowedYear(Year value){
            allowedYear = value;
            return this;
        }

        public RegisteringPeriodDTO build(){
            return new RegisteringPeriodDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public RegisteringPeriodDTO(){}

    private RegisteringPeriodDTO(Builder builder){
        period = builder.period;
        allowedYear = builder.allowedYear;
        id = builder.id;
    }

    public long getID() {
        return id;
    }

    public PeriodDTO getPeriodDTO() {
        return period;
    }

    public Year getAllowedYear() {
        return allowedYear;
    }
}
