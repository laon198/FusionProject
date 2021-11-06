package infra.dto;

import domain.model.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class dtoMapperTest {

    @DisplayName("Course 매핑 테스트")
    @Test
    public void courseMappingTest() throws NoSuchFieldException, IllegalAccessException {
        Course c = new Course(1, 3);
        CourseDTO cDTO = ModelMapper.courseToDTO(c);
        System.out.print(cDTO);
    }
}
