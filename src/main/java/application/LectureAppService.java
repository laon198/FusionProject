package application;

import domain.model.*;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.PlannerPeriodRepository;
import domain.repository.ProfessorRepository;
import domain.service.LectureManageService;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorCodeOption;
import infra.dto.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void create(LectureDTO lectureDTO){
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

        Lecture lecture = m.create(
                course,
                lectureDTO.getLectureCode(),
                prof,
                lectureDTO.getLimit(),
                times
        );

        lectureRepo.insert(lecture);
    }

    public void delete(LectureDTO lectureDTO){
        lectureRepo.remove(Lecture.builder().id(lectureDTO.getId()).build());
    }

    public LectureDTO retrieveByID(long id){
        return ModelMapper.lectureToDTO(lectureRepo.findByID(id));
    }

    public LectureDTO[] retrieveAll(){
        return lectureListToDTOArr(lectureRepo.findAll());
    }

    public LectureDTO[] retrieveByOption(LectureOption... options){
        return lectureListToDTOArr(lectureRepo.findByOption(options));
    }

    public LectureDTO[] getRegisteredLectures(RegisteringDTO[] regs){
        LectureDTO[] res = new LectureDTO[regs.length];

        for(int i=0; i<res.length; i++){
            res[i] = ModelMapper.lectureToDTO(lectureRepo.findByID(regs[i].getLectureID()));
        }

        return res;
    }

    private LectureDTO[] lectureListToDTOArr(List<Lecture> lectures){
        LectureDTO[] dtos = new LectureDTO[lectures.size()];

        for(int i=0; i<dtos.length; i++){
            dtos[i] = ModelMapper.lectureToDTO(lectures.get(i));
        }

        return dtos;
    }

    public PeriodDTO retrievePlannerPeriod(){
        return ModelMapper.periodToDTO(plannerPeriodRepo.find());
    }

    public void changePlannerPeriod(PeriodDTO periodDTO){
        Period period = new Period(periodDTO.getBeginTime(), periodDTO.getEndTime());
        plannerPeriodRepo.delete();
        plannerPeriodRepo.save(period);
    }

    public void update(LectureDTO lectureDTO){
        Lecture lecture = lectureRepo.findByID(lectureDTO.getId());

        //강의 계획서 업데이트
        LecturePlannerDTO plannerDTO = lectureDTO.getPlanner();
        lecture.writePlanner("goal", plannerDTO.getGoal());
        lecture.writePlanner("summary", plannerDTO.getSummary());
        //TODO : 업데이트할 항목 추가필요

        lectureRepo.save(lecture);
    }
}