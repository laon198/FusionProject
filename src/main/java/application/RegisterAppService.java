package application;

import domain.model.*;
import domain.repository.*;
import domain.service.Registrar;

import java.util.HashSet;
import java.util.Set;

public class RegisterAppService {
    private LectureRepository lectureRepo;
    private StudentRepository stdRepo;
    private CourseRepository courseRepo;
    private RegisteringRepository regRepo;
    private RegPeriodRepository periodRepo;

    public RegisterAppService(
            LectureRepository lectureRepo,
            StudentRepository stdRepo,
            CourseRepository courseRepo,
            RegisteringRepository regRepo,
            RegPeriodRepository periodRepo
    ){
        this.lectureRepo = lectureRepo;
        this.stdRepo = stdRepo;
        this.courseRepo = courseRepo;
        this.regRepo = regRepo;
        this.periodRepo = periodRepo;
    }

    public void register(long lectureID, long studentID){
        Lecture lecture = lectureRepo.findByID(lectureID);
        Student student = stdRepo.findByID(studentID);
        Set<RegisteringPeriod> pSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, pSet);
        Registering reg =  registrar.register(lecture, student);

        regRepo.save(reg);
    }

    public void cancel(long regID, long lectureID, long studentID){
        Lecture lecture = lectureRepo.findByID(lectureID);
        Student student = stdRepo.findByID(studentID);
        Registering registering = regRepo.findByID(regID);
        Set<RegisteringPeriod> pSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, pSet);
        Registering reg =  registrar.cancel(registering, student, lecture);

        regRepo.remove(reg);
    }

}
