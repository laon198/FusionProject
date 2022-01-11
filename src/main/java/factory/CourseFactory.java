package factory;

import domain.model.Course;
import dto.CourseDTO;

public class CourseFactory {
    public Course create(CourseDTO dto){
        return Course.builder(
                dto.getCourseCode(),
                dto.getCourseName(),
                dto.getDepartment(),
                dto.getTargetYear(),
                dto.getCredit())
                .build();

    }
}
