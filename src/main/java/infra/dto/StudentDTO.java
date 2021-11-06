package infra.dto;

import domain.generic.LectureTime;
import domain.model.Student;

import java.util.List;
import java.util.Set;

public class StudentDTO {
    private long id;
    private int maxCredit = 21;
    private int credit;
    private Student.Year year;
//    private List<Long> registeredLectureIDs;
//    private Set<LectureTime> timeTable;
}
