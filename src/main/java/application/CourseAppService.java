package application;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.dto.CourseDTO;
import infra.dto.ModelMapper;

import java.util.List;

//교과목과 관련된 기능을 수행하는 객체
public class CourseAppService {
    private final CourseRepository courseRepo;

    public CourseAppService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    //교과목 생성 기능
    public void create(CourseDTO courseDTO) {
        //받은 정보로 교과목 생성
        Course course = Course.builder()
                .courseCode(courseDTO.getCourseCode())
                .courseName(courseDTO.getCourseName())
                .department(courseDTO.getDepartment())
                .targetYear(courseDTO.getTargetYear())
                .credit(courseDTO.getCredit())
                .build();

        //생성한 교과목 데이터베이스에 저장
        courseRepo.save(course);
    }

    //교과목 수정 기능
    public void update(CourseDTO courseDTO) {
        //받은 정보로 교과목 조회
        Course course = courseRepo.findByID(courseDTO.getId());
        //받은 정보로 해당 교과목 수정
        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseName(courseDTO.getCourseName());
        course.setDepartment(courseDTO.getDepartment());
        course.setTargetYear(courseDTO.getTargetYear());
        course.setCredit(courseDTO.getCredit());

        //수정된 교과목 저장
        courseRepo.save(course);
    }

    //교과목 삭제 기능
    public void delete(CourseDTO courseDTO) {
        //받은 정보로 삭제할 교과목(id만 가짐) 임의로 생성
        Course course = Course.builder().id(courseDTO.getId()).build();
        //해당 교과목 삭제
        courseRepo.remove(course);
    }

    // 교과목 전체조회
    public CourseDTO[] retrieveAll() {
        //교과목 전체조회하여 DTO배열로 반환
        return courseListToDTOList(courseRepo.findAll());
    }

    //교과목 id로 조회
    public CourseDTO retrieveByID(long id) {
        //교과목 id로 조회하여 DTO로 변환하여 반환
        return ModelMapper.courseToDTO(courseRepo.findByID(id));
    }

    //교과목 list를 교과목DTO 배열로 변환
    private CourseDTO[] courseListToDTOList(List<Course> courses) {
        CourseDTO[] arr = new CourseDTO[courses.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = ModelMapper.courseToDTO(courses.get(i));
        }

        return arr;
    }
}
