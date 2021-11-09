package infra.dto;

import domain.generic.LectureTime;
import domain.model.Registering;
import domain.model.Student;

import java.time.LocalDate;
import java.util.*;

public class StudentDTO extends MemberDTO {
    private int maxCredit = 21;
    private int credit;
    private int year;
    private String studentCode;
    private Set<RegisteringDTO> myRegisterings;
    private Set<LectureTimeDTO> timeTable;

    public static class Builder{
        private Long id;
        private int credit;
        private int maxCredit=21;
        private int year;
        private String name;
        private String department;
        private String studentCode;
        private String birthDate;
        private Set<RegisteringDTO> myRegisterings = new HashSet<>();
        private Set<LectureTimeDTO> timeTable = new HashSet<>();

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

        public Builder year(int value){
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
            myRegisterings = value;
            return this;
        }

        public Builder timeTable(Set<LectureTimeDTO> timeTable){
            timeTable = new HashSet(timeTable);
            return this;
        }

        public StudentDTO build(){
            return new StudentDTO(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

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

    public int getYear() {
        return year;
    }

    public int getCredit() {
        return credit;
    }

    public int getMaxCredit() {
        return maxCredit;
    }
}
