package domain.model;

import java.util.Objects;

public class CourseID {
    private long id;

    public CourseID(long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseID courseID = (CourseID) o;
        return id == courseID.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
