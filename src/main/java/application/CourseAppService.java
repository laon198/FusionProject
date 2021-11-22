package application;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CourseAppService {
    private CourseRepository courseRepo;

    public CourseAppService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public void create(CourseDTO courseDTO){
        Course course = Course.builder()
                        .courseCode(courseDTO.getCourseCode())
                        .courseName(courseDTO.getCourseName())
                        .department(courseDTO.getDepartment())
                        .targetYear(courseDTO.getTargetYear())
                        .credit(courseDTO.getCredit())
                        .build();

        courseRepo.save(course);
    }

    public void update(CourseDTO courseDTO){
        Course course = Course.builder()
                .id(courseDTO.getId())
                .courseCode(courseDTO.getCourseCode())
                .courseName(courseDTO.getCourseName())
                .department(courseDTO.getDepartment())
                .targetYear(courseDTO.getTargetYear())
                .credit(courseDTO.getCredit())
                .build();

        courseRepo.save(course);
    }

    public void delete(CourseDTO courseDTO){
        Course course = Course.builder().id(courseDTO.getId()).build();
        courseRepo.remove(course);
    }

    public List<CourseDTO> retrieveAll(){
        return courseListToDTOList(courseRepo.findAll());
    }

    public CourseDTO retrieveByID(long id){
        return ModelMapper.courseToDTO(courseRepo.findByID(id));
    }

    private List<CourseDTO> courseListToDTOList(List<Course> courses){
        List<CourseDTO> courseDTOS = new ArrayList<>();

        for(Course c : courses){
            courseDTOS.add(
                    ModelMapper.courseToDTO(c)
            );
        }

        return courseDTOS;
    }
}
