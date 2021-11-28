package application;

import domain.model.Period;
import domain.model.*;
import domain.repository.*;
import domain.service.Registrar;
import infra.database.option.student.StudentCodeOption;
import infra.dto.RegisteringDTO;
import infra.dto.RegisteringPeriodDTO;

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

    public void register(RegisteringDTO regDTO) throws IllegalStateException, IllegalArgumentException{
        Lecture lecture = lectureRepo.findByID(regDTO.getLectureID());
        Student student = stdRepo.findByOption(new StudentCodeOption(regDTO.getStudentCode())).get(0);
        Set<RegisteringPeriod> periodSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, periodSet);
        Registering reg =  registrar.register(lecture, student);

        regRepo.save(reg);
        stdRepo.save(student);
    }

    public boolean isValidPeriodAbout(Student std, Course course){
        Set<RegisteringPeriod> pSet = new HashSet<>(periodRepo.findAll());
        Registrar registrar = new Registrar(lectureRepo, courseRepo, pSet);

        return registrar.isValidPeriodAbout(std, course);
    }

    public void cancel(RegisteringDTO regDTO) throws IllegalArgumentException{
        Lecture lecture = lectureRepo.findByID(regDTO.getLectureID());

        Student student = stdRepo.findByOption(
                new StudentCodeOption(regDTO.getStudentCode())).get(0);
        Registering registering = regRepo.findByID(regDTO.getId());
        Set<RegisteringPeriod> periodSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, periodSet);
        registrar.cancel(registering, student, lecture);

        regRepo.remove(registering);
        stdRepo.save(student);
    }

    public void addRegisteringPeriod(RegisteringPeriodDTO rPeriodDTO){
        RegisteringPeriod newPeriod = RegisteringPeriod.builder()
                                    .period(
                                            new Period(
                                                    rPeriodDTO.getPeriodDTO().getBeginTime(),
                                                    rPeriodDTO.getPeriodDTO().getEndTime()
                                            ))
                                    .allowedYear(rPeriodDTO.getAllowedYear())
                                    .build();

        Set<RegisteringPeriod> existingSet = new HashSet<>(periodRepo.findAll());
        if(existingSet.contains(newPeriod)){
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }

        periodRepo.save(newPeriod);
    }

    public void removeRegisteringPeriod(RegisteringPeriodDTO rPeriodDTO) {
        RegisteringPeriod target = RegisteringPeriod.builder()
                .period(
                        new Period(
                                rPeriodDTO.getPeriodDTO().getBeginTime(),
                                rPeriodDTO.getPeriodDTO().getEndTime()
                        ))
                .allowedYear(rPeriodDTO.getAllowedYear())
                .id(rPeriodDTO.getID())
                .build();
        //TODO : 존재안할경우 예외 발생
        periodRepo.remove(target);
    }


}
