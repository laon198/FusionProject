package application;

import domain.generic.Period;
import domain.model.Lecture;
import domain.model.LectureID;
import domain.repository.LectureRepository;
import domain.service.RegisterService;
import domain.service.RegisteringPeriod;

public class AdminService {
    LectureRepository lectureRepo;
    RegisterService registrar;

    public AdminService(LectureRepository lectureRepo,
                            RegisterService registrar){
        this.lectureRepo = lectureRepo;
        this.registrar = registrar;
    }

    public void setLecturePlannerPeriod(LectureID lectureID, Period period){
        Lecture targetLecture = lectureRepo.findByID(lectureID);
        targetLecture.setPlannerWritingPeriod(period);
    }

    //TODO : 수강신청기간 삭제 및 추가도 생각해야함
    public void addRegisteringPeriod(RegisteringPeriod rPeriod){
        registrar.addRegisteringPeriod(rPeriod);
    }

}
