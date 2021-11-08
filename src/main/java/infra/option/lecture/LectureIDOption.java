package infra.option.lecture;

public class LectureIDOption implements LectureOption {
    private String query = "lecture_sq=";

    public LectureIDOption(String option) {
        query += option;
    }

    public String getQuery() {
        return query;
    }
}
