package infra.dto;

import domain.generic.LectureTime;

import java.util.*;

public class ProfessorDTO extends MemberDTO {
    private String professorCode;

    public static class Builder{
        private Long id;
        private String name;
        private String department;
        private String birthDate;
        private String professorCode;
//        private Set<LectureTime> timeTable = new HashSet<>();

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

        public Builder professorCode(String value){
            professorCode = value;
            return this;
        }

//        public Builder timeTable(LectureTime... lectureTimes){
//            timeTable = new HashSet(Arrays.asList(lectureTimes));
//            return this;
//        }

        public ProfessorDTO build(){
            return new ProfessorDTO(this);
        }
    }//end of builder class

    public static Builder builder(){
        return new Builder();
    }

    private ProfessorDTO(Builder builder){
        super(builder.id, builder.name,
                builder.department, builder.birthDate);
//            timeTable = builder.timeTable;
        professorCode = builder.professorCode;
    }

    public String getProfessorCode() {
        return professorCode;
    }
}
