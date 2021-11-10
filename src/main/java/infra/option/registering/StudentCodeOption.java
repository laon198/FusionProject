package infra.option.registering;

public class StudentCodeOption implements RegisteringOption {
    private String query="student_code=";

    public StudentCodeOption(String value){
        query+=value;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
