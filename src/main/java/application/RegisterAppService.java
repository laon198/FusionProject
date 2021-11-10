package application;

import domain.generic.LectureTime;
import domain.generic.Period;
import domain.model.*;
import domain.repository.*;
import domain.service.Registrar;
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

    public void register(long lectureID, long studentID){
//        Lecture lecture = lectureRepo.findByID(lectureID);
//        Set<LectureTime> times = new HashSet<>();
//        times.add(LectureTime.builder().startTime(1).endTime(2).lectureDay("MON").build());
//        times.add(LectureTime.builder().startTime(3).endTime(4).lectureDay("FRI").build());
//        Lecture lecture = Lecture.builder()
//                            .id(lectureID)
//                            .limit(0)
//                            .courseID(2)
//                            .lecturerID("p222")
//                            .lectureCode("SE222")
//                            .lectureTimes(times)
//                            .build();
        Set<LectureTime> times = new HashSet<>();
        times.add(LectureTime.builder().startTime(1).endTime(2).lectureDay("TUE").build());
        times.add(LectureTime.builder().startTime(4).endTime(5).lectureDay("FRI").build());
        Lecture lecture = Lecture.builder()
                .id(lectureID)
                .limit(30)
                .courseID(1)
                .lecturerID("p333")
                .lectureCode("SE555")
                .lectureTimes(times)
                .build();

        Student student = stdRepo.findByID(studentID);
        Set<RegisteringPeriod> pSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, pSet);
        Registering reg =  registrar.register(lecture, student);

        regRepo.save(reg);
    }

    public void cancel(long regID, long lectureID, long studentID){
//        Lecture lecture = lectureRepo.findByID(lectureID);
        Set<LectureTime> times = new HashSet<>();
        times.add(LectureTime.builder().startTime(1).endTime(2).lectureDay("TUE").build());
        times.add(LectureTime.builder().startTime(4).endTime(5).lectureDay("FRI").build());
        Lecture lecture = Lecture.builder()
                .id(lectureID)
                .limit(30)
                .courseID(1)
                .lecturerID("p333")
                .lectureCode("SE555")
                .lectureTimes(times)
                .build();
        Student student = stdRepo.findByID(studentID);
        Registering registering = regRepo.findByID(regID);
        Set<RegisteringPeriod> pSet = new HashSet<>(periodRepo.findAll());

        Registrar registrar = new Registrar(lectureRepo, courseRepo, pSet);
        Registering reg =  registrar.cancel(registering, student, lecture);

        regRepo.remove(reg);
    }

    //TODO : 겹치는 시간 처리? 좀더 잘처리
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
