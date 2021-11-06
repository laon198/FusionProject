package application;


import domain.model.LectureID;
import domain.model.StudentID;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.StudentRepository;
import domain.service.RegisterService;

public class StudentService {
    RegisterService registrar;
    LectureRepository lectureRepo;
    CourseRepository courseRepo;
    StudentRepository studentRepo;

    public StudentService(LectureRepository lectureRepo, CourseRepository courseRepo,
                          StudentRepository studentRepo){
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
        this.registrar = new RegisterService(lectureRepo, courseRepo, studentRepo);
    }

    public void register(long lectureID, long studentID){
        registrar.register(lectureID, studentID);
    }
}
