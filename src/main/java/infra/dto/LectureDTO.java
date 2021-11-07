package infra.dto;

import domain.generic.LectureTime;
import domain.model.LecturePlanner;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class LectureDTO {
    private long id;
    private long courseID;
    private long lecturerID;
    private int limit;
    private Set<LectureTime> lectureTimes;
    private List<Long> registeredStudentIDs;
    private LecturePlanner planner;

    public LectureDTO(long lectureID, long professorID, int limitPersonNum, long courseID,
                      Set<LectureTime> lectureTimes) {
        id = lectureID;
        lecturerID = professorID;
        limit = limitPersonNum;
        registeredStudentIDs = new ArrayList<>(limitPersonNum);
        this.lectureTimes = lectureTimes;
        this.courseID = courseID;
        planner = new LecturePlanner();
    }
}
