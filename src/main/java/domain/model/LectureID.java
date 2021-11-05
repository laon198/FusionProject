package domain.model;

import java.util.Objects;

public class LectureID {
    private final String id;

    public LectureID(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureID lectureID = (LectureID) o;
        return Objects.equals(id, lectureID.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
