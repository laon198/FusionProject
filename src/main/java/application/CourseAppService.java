package application;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CourseAppService {
    private final CourseRepository courseRepo;

    public CourseAppService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public void create(CourseDTO courseDTO){
        //TODO : 생성 validation 체크필요
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
        Course course = courseRepo.findByID(courseDTO.getId());

        //TODO : 업데이트될 항목추가 필요

        courseRepo.save(course);
    }

    public void delete(CourseDTO courseDTO){
        Course course = Course.builder().id(courseDTO.getId()).build();
        courseRepo.remove(course);
    }

    public CourseDTO[] retrieveAll(){
        return courseListToDTOList(courseRepo.findAll());
    }

    public CourseDTO retrieveByID(long id){
        return ModelMapper.courseToDTO(courseRepo.findByID(id));
    }

    private CourseDTO[] courseListToDTOList(List<Course> courses){
        CourseDTO[] arr = new CourseDTO[courses.size()];

        for(int i=0; i<arr.length; i++){
            arr[i] = ModelMapper.courseToDTO(courses.get(i));
        }

        return arr;
    }
}
