package dto;

import domain.model.Year;

import java.util.*;

public class StudentDTO extends MemberDTO {
    private int maxCredit = 21;
    private int credit;
    private Year year;
    private String studentCode;
    private RegisteringDTO[] myRegisterings;
    private LectureTimeDTO[] timeTable;

    public static class Builder{
        private Long id=-1L;
        private int credit;
        private int maxCredit=21;
        private Year year;
        private String name;
        private String department;
        private String studentCode;
        private String birthDate;
        private RegisteringDTO[] myRegisterings;
        private LectureTimeDTO[] timeTable;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder name(String value){
            name = value;
            return this;
        }

        public Builder department(String value){
            department = value;
            return this;
        }

        public Builder birthDate(String value){
            birthDate = value;
            return this;
        }

        public Builder studentCode(String value){
            studentCode = value;
            return this;
        }

        public Builder year(Year value){
            year = value;
            return this;
        }

        public Builder credit(int value){
            credit = value;
            return this;
        }

        public Builder maxCredit(int value){
            maxCredit = value;
            return this;
        }

        public Builder myRegisterings(Set<RegisteringDTO> value){
            myRegisterings = value.toArray(new RegisteringDTO[value.size()]);
            return this;
        }

        public Builder timeTable(Set<LectureTimeDTO> value){
            timeTable = value.toArray(new LectureTimeDTO[value.size()]);
            return this;
        }

        public StudentDTO build(){
            return new StudentDTO(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    public StudentDTO(){}

    private StudentDTO(Builder builder){
            super(builder.id, builder.name,
                    builder.department, builder.birthDate);
            year = builder.year;
            myRegisterings = builder.myRegisterings;
            timeTable = builder.timeTable;
            credit = builder.credit;
            maxCredit = builder.maxCredit;
            studentCode = builder.studentCode;
        }

    public String getStudentCode() {
        return studentCode;
    }

    @Override
    public String getCode() {
        return studentCode;
    }

    public Year getYear() {
        return year;
    }

    public int getCredit() {
        return credit;
    }

    public int getMaxCredit() {
        return maxCredit;
    }

    public RegisteringDTO[] getMyRegisterings() {
        return myRegisterings;
    }

    public LectureTimeDTO[] getTimeTable() {
        return timeTable;
    }
}

