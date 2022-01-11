package application;

import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.PlannerPeriodRepository;
import domain.repository.ProfessorRepository;
import domain.service.LectureManageService;
import dto.*;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorCodeOption;
import infra.dto.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//개설교과목과 관련된 기능을 수행하는 객체
public class LectureAppService {
    private LectureRepository lectureRepo;
    private CourseRepository courseRepo;
    private ProfessorRepository profRepo;
    private PlannerPeriodRepository plannerPeriodRepo;

    public LectureAppService(
            LectureRepository lectureRepo, CourseRepository courseRepo,
            ProfessorRepository profRepo, PlannerPeriodRepository plannerPeriodRepo) {
        this.lectureRepo = lectureRepo;
        this.courseRepo = courseRepo;
        this.profRepo = profRepo;
        this.plannerPeriodRepo = plannerPeriodRepo;
    }

    //개설교과목 생성 기능수행
    public void create(LectureDTO lectureDTO){
        //개설교과목 생성 로직을 수행하는 객체 생성
        LectureManageService m = new LectureManageService(
                lectureRepo
        );

        Set<LectureTime> times = new HashSet<>();
        for(LectureTimeDTO timeDTO : lectureDTO.getLectureTimes()){
            times.add(
                    LectureTime.builder()
                    .lectureDay(timeDTO.getLectureDay())
                    .endTime(timeDTO.getEndTime())
                    .startTime(timeDTO.getStartTime())
                    .room(timeDTO.getRoom())
                    .lectureName(timeDTO.getLectureName())
                    .build()
            );
        }

        Course course = courseRepo.findByID(lectureDTO.getCourseID());
        Professor prof = profRepo.findByOption(new ProfessorCodeOption(lectureDTO.getProfessorCode())).get(0);

        //개설교과목 생성
        Lecture lecture = m.create(
                course,
                lectureDTO.getLectureCode(),
                prof,
                lectureDTO.getLimit(),
                times
        );

        //생성된 개설교과목 데이터베이스에 저장
        lectureRepo.insert(lecture);
    }

    //개셜교과목 삭제
    public void delete(LectureDTO lectureDTO){
        //받은 정보를 통해 개설교과목 삭제
        lectureRepo.remove(Lecture.builder().id(lectureDTO.getId()).build());
    }

    //id를 통해 개설교과목 조회
    public LectureDTO retrieveByID(long id){
        //id에 해당하는 개설교과목을 DTO로 반환
        return ModelMapper.lectureToDTO(lectureRepo.findByID(id));
    }

    //전체 개설교과목 조회
    public LectureDTO[] retrieveAll(){
        //전체 개설교과목을 DTO배열로 반환
        return lectureListToDTOArr(lectureRepo.findAll());
    }

    //조건으로 개설교과목 조회
    public LectureDTO[] retrieveByOption(LectureOption... options){
        //조건에 해당하는 개설교과목을 DTO배열로 반환
        return lectureListToDTOArr(lectureRepo.findByOption(options));
    }

    //수강신청한 교과목 조회
    public LectureDTO[] getRegisteredLectures(Student std){
        //해당학생의 수강신청 내역 가져옴
        Set<Registering> regs = std.getMyRegisterings();
        LectureDTO[] res = new LectureDTO[regs.size()];

        //수강신청 내역에 있는 개설교과목 id를통해 개설교과목 조회
        //개설교과목DTO배열로 만들어 반환
        int i=0;
        for(Registering reg : regs){
            res[i++] = ModelMapper.lectureToDTO(lectureRepo.findByID(reg.getLectureID()));
        }

        return res;
    }

    //개설교과목 수정
    public void update(LectureDTO lectureDTO){
        //DTO에있는 id값으로 개설교과목 조회
        Lecture lecture = lectureRepo.findByID(lectureDTO.getId());

        //강의 계획서 수정
        LecturePlannerDTO plannerDTO = lectureDTO.getPlanner();
        lecture.writePlanner("goal", plannerDTO.getGoal());
        lecture.writePlanner("summary", plannerDTO.getSummary());

        //개설교과목 수정
        lecture.setLectureCode(lectureDTO.getLectureCode());
        lecture.setLimit(lectureDTO.getLimit());

        //개설교과목 담당교수 수정
        Professor prof = profRepo.findByID(lectureDTO.getProfessor().getId());
        lecture.setProfessor(prof);

        //강의시간변경
        Set<LectureTime> times = new HashSet<>();
        for(LectureTimeDTO dto : lectureDTO.getLectureTimes()){
            times.add(
                    LectureTime.builder()
                            .id(dto.getId())
                            .startTime(dto.getStartTime())
                            .endTime(dto.getEndTime())
                            .lectureDay(dto.getLectureDay())
                            .room(dto.getRoom())
                            .lectureName(dto.getLectureName())
                            .build()
            );
        }
        lecture.setLectureTimes(times);


        //수정된 개설교과목 데이터베이스에 저장
        lectureRepo.save(lecture);
    }

    //강의계획서 입력기간 조회
    public PeriodDTO retrievePlannerPeriod(){
        //강의계획서 입력기간 조회하여 DTO로 반환
        return ModelMapper.periodToDTO(plannerPeriodRepo.find());
    }

    //강의계회것 입력기간 변경
    public void changePlannerPeriod(PeriodDTO periodDTO){
        //현재 강의계획서 입력기간을 받은 입력기간으로 변경
        Period period = new Period(periodDTO.getBeginTime(), periodDTO.getEndTime());
        plannerPeriodRepo.delete();
        plannerPeriodRepo.save(period);
    }

    //개설교과목 list를 DTO배열로 변환
    private LectureDTO[] lectureListToDTOArr(List<Lecture> lectures){
        LectureDTO[] dtos = new LectureDTO[lectures.size()];

        for(int i=0; i<dtos.length; i++){
            dtos[i] = ModelMapper.lectureToDTO(lectures.get(i));
        }

        return dtos;
    }

}