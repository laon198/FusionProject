package infra.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LectureDTO {

    private long id;
    private long courseID;
    private long lectureCode;
    private int limit;
    private int applicantCount;
    private long plannerID;
    private long professorCode;

    public LectureDTO(long id, long courseID, long lectureCode, int limit, int applicantCount, long plannerID, long professorCode) {
        this.id = id;
        this.courseID = courseID;
        this.lectureCode = lectureCode;
        this.limit = limit;
        this.applicantCount = applicantCount;
        this.plannerID = plannerID;
        this.professorCode = professorCode;
    }

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
