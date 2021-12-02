package application;

import domain.model.Period;
import domain.model.*;
import domain.repository.*;
import domain.service.Registrar;
import infra.database.option.student.StudentCodeOption;
import infra.dto.ModelMapper;
import infra.dto.RegisteringDTO;
import infra.dto.RegisteringPeriodDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//수강신청과 관련 기능을 수행하는 객체
public class RegisterAppService {
    private final LectureRepository lectureRepo;
    private final StudentRepository stdRepo;
    private final CourseRepository courseRepo;
    private final RegisteringRepository regRepo;
    private final RegPeriodRepository periodRepo;

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

    //수강신청기능
    synchronized public void register(RegisteringDTO regDTO) throws IllegalStateException, IllegalArgumentException{
        //registeringDTO에있는 개설교과목 id와 학번으로 해당 개설교과목과 학생 객체 가져옴
        Lecture lecture = lectureRepo.findByID(regDTO.getLectureID());
        Student student = stdRepo.findByOption(new StudentCodeOption(regDTO.getStudentCode())).get(0);
        //수강신청 기간 가져옴
        Set<RegisteringPeriod> periodSet = new HashSet<>(periodRepo.findAll());

        //수강신청 기능을 수행하는 객체 생성
        Registrar registrar = new Registrar(lectureRepo, courseRepo, periodSet);
        //수강신청
        Registering reg =  registrar.register(lecture, student);

        //수강신청이후 값이 변경된 학생과, 수강신청 데이터베이스에 저장
        regRepo.save(reg);
        stdRepo.save(student);
    }

    //수강취소기능
    public void cancel(RegisteringDTO regDTO) throws IllegalArgumentException{
        //registeringDTO에있는 id로 개설교과목과 학생객체 가져옴
        //registeringDTO에있는 id로 해당 수강신청 객체 가져옴
        Lecture lecture = lectureRepo.findByID(regDTO.getLectureID());
        Student student = stdRepo.findByOption(
                new StudentCodeOption(regDTO.getStudentCode())).get(0);
        Registering registering = regRepo.findByID(regDTO.getId());
        //수강신청 기간 가져옴
        Set<RegisteringPeriod> periodSet = new HashSet<>(periodRepo.findAll());

        //수강취소 수행하는 객체 생성
        Registrar registrar = new Registrar(lectureRepo, courseRepo, periodSet);
        //수강취소
        registrar.cancel(registering, student, lecture);

        //수강취소된 수강객체 데이터베이스에서 삭제
        regRepo.remove(registering);
        //수강취소된 학생 변경사항 저장
        stdRepo.save(student);
    }

    //수강신청 기간 추가
    public void addRegisteringPeriod(RegisteringPeriodDTO rPeriodDTO){
        //받은 정보로 수강신청 기간 객체 생성
        RegisteringPeriod newPeriod = RegisteringPeriod.builder()
                                    .period(
                                            new Period(
                                                    rPeriodDTO.getPeriodDTO().getBeginTime(),
                                                    rPeriodDTO.getPeriodDTO().getEndTime()
                                            ))
                                    .allowedYear(rPeriodDTO.getAllowedYear())
                                    .build();

        //현재 존재하는 수강신청기간과 중복되는지 확인
        Set<RegisteringPeriod> existingSet = new HashSet<>(periodRepo.findAll());
        if(existingSet.contains(newPeriod)){
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }

        //새로운 수강신청기간 데이터베이스에 저장
        periodRepo.save(newPeriod);
    }

    //수강신청기간조회
    public RegisteringPeriodDTO[] retrieveRegPeriodAll(){
        //수강신청기간조회
        List<RegisteringPeriod> regPeriods = periodRepo.findAll();
        RegisteringPeriodDTO[] dtos = new RegisteringPeriodDTO[regPeriods.size()];

        //수강신청기간 DTO로 변환하여 배열로 반환
        for(int i=0; i<dtos.length; i++){
            dtos[i] = ModelMapper.regPeriodToDTO(regPeriods.get(i));
        }

        return dtos;
    }

    //수강신청기간 삭제
    public void removeRegisteringPeriod(RegisteringPeriodDTO rPeriodDTO) {
        //받은 DTO의 id값으로 수강신청기간 객체 생성
        RegisteringPeriod target = RegisteringPeriod.builder()
                .id(rPeriodDTO.getID())
                .build();

        //데이터베이스에서 해당 수강신청기간 삭제
        periodRepo.remove(target);
    }
}
