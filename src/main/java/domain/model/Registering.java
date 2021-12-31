package domain.model;

import java.util.Objects;

public class Registering {
    private final long id;
    private final long lectureID;
    private final String studentCode;
    private final String registeringTime;

    public static class Builder{
        private long id;
        private long lectureID;
        private String studentCode;
        private String registeringTime;

        private Builder(long lectureID, String studentCode, String registeringTime){
            this.lectureID = lectureID;
            this.studentCode = studentCode;
            this.registeringTime = registeringTime;
        }

        public Builder id(long value){
            id = value;
            return this;
        }

        public Registering build(){
            return new Registering(this);
        }
    }

    public static Builder builder(long lectureID, String studentCode, String registeringTime){
        return new Builder(lectureID, studentCode, registeringTime);
    }

    public Registering(Builder builder){
        id = builder.id;
        lectureID = builder.lectureID;
        studentCode = builder.studentCode;
        registeringTime = builder.registeringTime;
    }

    public long getID() {
        return id;
    }

    public long getLectureID() {
        return lectureID;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getRegisteringTime() {
        return registeringTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registering that = (Registering) o;
        return id == that.id && lectureID == that.lectureID && studentCode == that.studentCode && Objects.equals(registeringTime, that.registeringTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureID, studentCode, registeringTime);
    }
}
