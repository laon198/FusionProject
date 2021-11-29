package infra.database.option.registering;

public class LectureIDOption implements RegisteringOption {
    private String query="lecture_PK=";

    public LectureIDOption(long value){
        query+=value;
    }

    @Override
    public String getQuery() {
        return query;
    }
}
