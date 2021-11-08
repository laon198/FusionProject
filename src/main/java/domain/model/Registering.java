package domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Registering {
    private final long id;
    private final long lectureID;
    private final long studentID;
    private final LocalDateTime registeringTime;

    public static class Builder{
        private long id;
        private long lectureID;
        private long studentID;
        private LocalDateTime registeringTime;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder lectureID(long value){
            id = value;
            return this;
        }

        public Builder studentID(long value){
            studentID = value;
            return this;
        }

        public Registering build(){
            return new Registering(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public Registering(Builder builder){
        id = builder.id;
        lectureID = builder.lectureID;
        studentID = builder.studentID;
        registeringTime = LocalDateTime.now();
    }

    public long getLectureID() {
        return lectureID;
    }

    public long getStudentID() {
        return studentID;
    }

    public LocalDateTime getRegisteringTime() {
        return registeringTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registering that = (Registering) o;
        return id == that.id && lectureID == that.lectureID && studentID == that.studentID && Objects.equals(registeringTime, that.registeringTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureID, studentID, registeringTime);
    }


}
