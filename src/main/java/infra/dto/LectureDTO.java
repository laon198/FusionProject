package infra.dto;

import domain.generic.LectureTime;
import domain.model.LecturePlanner;
import domain.model.Registering;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class LectureDTO {
    private long id;
    private long courseID;
    private String lectureCode;
    private String lecturerID;
    private int limit;
    private Set<LectureTimeDTO> lectureTimes;
    private Set<RegisteringDTO> myRegisterings;
    private LecturePlanner planner;


    public static class Builder {
        private long id;
        private long courseID;
        private String lectureCode;
        private String lecturerID;
        private int limit;
        private Set<LectureTimeDTO> lectureTimes;
        private Set<RegisteringDTO> myRegisterings;
        private LecturePlanner planner;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder courseID(long courseID) {
            this.courseID = courseID;
            return this;
        }

        public Builder lectureCode(String lectureCode) {
            this.lectureCode = lectureCode;
            return this;
        }

        public Builder lecturerID(String lecturerID) {
            this.lecturerID = lecturerID;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder lectureTimes(Set<LectureTimeDTO> lectureTimes) {
            this.lectureTimes = lectureTimes;
            return this;
        }

        public Builder registerings(Set<RegisteringDTO> registerings) {
            myRegisterings = registerings;
            return this;
        }

        public Builder planner(LecturePlanner planner) {
            this.planner = planner;
            return this;
        }

        public LectureDTO build(){
            return new LectureDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LectureDTO(LectureDTO.Builder builder){
        id = builder.id;
        courseID = builder.courseID;
        lectureCode = builder.lectureCode;
        lecturerID = builder.lecturerID;
        limit = builder.limit;
        lectureTimes = builder.lectureTimes;
        myRegisterings = builder.myRegisterings;
        planner = builder.planner;
    }

//    public LectureDTO(long id, long courseID, long lectureCode, int limit, int applicantCount, long plannerID, long professorCode) {
//        this.id = id;
//        this.courseID = courseID;
//        this.lectureCode = lectureCode;
//        this.limit = limit;
//        this.applicantCount = applicantCount;
//        this.plannerID = plannerID;
//        this.professorCode = professorCode;
//    }

    //TODO : 원래 있던 것

//    private long id;
//    private long courseID;
//    private long lecturerID;
//    private int limit;
//    private Set<LectureTime> lectureTimes;
//    private List<Long> registeredStudentIDs;
//    private LecturePlanner planner;
//
//    public LectureDTO(long lectureID, long professorID, int limitPersonNum, long courseID,
//                      Set<LectureTime> lectureTimes) {
//        id = lectureID;
//        lecturerID = professorID;
//        limit = limitPersonNum;
//        registeredStudentIDs = new ArrayList<>(limitPersonNum);
//        this.lectureTimes = lectureTimes;
//        this.courseID = courseID;
//        planner = new LecturePlanner();
//    }
}
