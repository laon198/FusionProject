package infra.dto;

import java.time.LocalDateTime;

public class PeriodDTO {
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    public static class Builder{
        private LocalDateTime beginTime;
        private LocalDateTime endTime;

        public Builder beginTime(LocalDateTime value){
            beginTime = value;
            return this;
        }

        public Builder endTime(LocalDateTime value){
            endTime = value;
            return this;
        }

        public PeriodDTO build(){
            return new PeriodDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private PeriodDTO(Builder builder){
        beginTime = builder.beginTime;
        endTime = builder.endTime;
    }
}
