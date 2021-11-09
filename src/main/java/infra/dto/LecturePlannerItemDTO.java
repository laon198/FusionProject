package infra.dto;

public class LecturePlannerItemDTO {
    private String name;
    private String content;

    public static class Builder{
        private String name;
        private String content;

        public Builder name(String value){
            name = value;
            return this;
        }

        public Builder content(String value){
            content = value;
            return this;
        }

        public LecturePlannerItemDTO build(){
            return new LecturePlannerItemDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LecturePlannerItemDTO(Builder builder){
        name = builder.name;
        content = builder.content;
    }
}
