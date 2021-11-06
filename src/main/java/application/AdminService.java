package application;

import domain.generic.LectureTime;
import domain.generic.Period;
import domain.model.Lecture;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.ProfessorRepository;
import domain.service.LectureManageService;
import domain.service.RegisterService;
import domain.service.RegisteringPeriod;

import java.util.Set;

public class AdminService {
    LectureRepository lectureRepo;
    RegisterService registrar;
    LectureManageService lManagerService;
    ProfessorRepository profRepo;
    CourseRepository courseRepo;

    public AdminService(LectureRepository lectureRepo, CourseRepository courseRepo,
                        RegisterService registrar, ProfessorRepository profRepo){
        this.lectureRepo = lectureRepo;
        this.registrar = registrar;
        this.profRepo = profRepo;
        this.courseRepo = courseRepo;
        lManagerService = new LectureManageService(courseRepo, profRepo, lectureRepo);
    }

    public void setLecturePlannerPeriod(long lectureID, Period period){
        Lecture targetLecture = lectureRepo.findByID(lectureID);
        targetLecture.setPlannerWritingPeriod(period);
    }

    //TODO : id로 하는게 나을까?
    public void addRegisteringPeriod(RegisteringPeriod rPeriod){
        registrar.addRegisteringPeriod(rPeriod);
    }

    //TODO : 해당하는 객체가 없을때 예외필요?
    public void removeRegisteringPeriod(RegisteringPeriod rPeriod){
        registrar.removeRegisteringPeriod(rPeriod);
    }

    public void addLecture(long lectureID, long courseID, long professorID,
                           int limit, Set<LectureTime> lectureTimes){
        lManagerService.addLecture(
                lectureID, courseID, professorID,
                    limit, lectureTimes
        );
    }

    public void removeLecture(long lectureId){
        lManagerService.removeLecture(lectureId);
    }

    public void updateAboutRoom(long lectureID, LectureTime targetLectureTime, String room){
        lManagerService.updateAboutRoom(lectureID, targetLectureTime, room);
    }

    public void updateAboutMax(long lectureID, int max){
        lManagerService.updateAboutMaxStd(lectureID, max);
    }

}