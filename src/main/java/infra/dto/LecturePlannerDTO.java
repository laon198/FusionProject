package infra.dto;

import domain.generic.Period;

import java.util.Set;

public class LecturePlannerDTO {
    private Set<LecturePlannerItemDTO> items;
    private static PeriodDTO writePeriod;

    public static class Builder{
        private Set<LecturePlannerItemDTO> items;
        private static PeriodDTO writePeriod;

        public Builder items(Set<LecturePlannerItemDTO> value){
            items = value;
            return this;
        }

        public Builder writePeriod(PeriodDTO value){
            writePeriod = value;
            return this;
        }

        public LecturePlannerDTO build(){
            return new LecturePlannerDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LecturePlannerDTO(Builder builder){
        items = builder.items;
        writePeriod = builder.writePeriod;
    }
}
