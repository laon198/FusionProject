package domain.model;

import java.util.Objects;

public class StudentID {
    private final String id;

    public StudentID(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentID studentID = (StudentID) o;
        return Objects.equals(id, studentID.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
