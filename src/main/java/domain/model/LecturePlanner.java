package domain.model;


public class LecturePlanner {
    private String goal;
    private String summary;

    private static class Builder{
        private String goal = " ";
        private String summary = " ";

        public Builder goal(String value){
            goal = value;
            return this;
        }

        public Builder summary(String value){
            summary = value;
            return this;
        }

        public LecturePlanner build(){
            return new LecturePlanner(this);
        }
    }

    public LecturePlanner(){
        goal = " ";
        summary = " ";
    }

    private LecturePlanner(Builder builder){
        goal = builder.goal;
        summary = builder.summary;
    }

    public static Builder builder(){
        return new Builder();
    }

    public void writeItem(String itemName, String content) {

        if(itemName.equals("goal")){
            goal = content;
        }else if(itemName.equals("summary")){
            summary = content;
        }else{
            throw new IllegalArgumentException("존재하지않는 항목입니다.");
        }
    }
}
