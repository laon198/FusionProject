package application;

import domain.model.Lecture;
import domain.repository.LectureRepository;

public class ProfessorService {
    LectureRepository lectureRepo;

    public ProfessorService(LectureRepository lectureRepo){
        this.lectureRepo = lectureRepo;
    }

    public void writeLecturePlanner(long lectureID, String itemName,
                                    String content, long writer){
        Lecture targetLecture = lectureRepo.findByID(lectureID);
        targetLecture.writePlanner(itemName, content, writer);
    }

    //TODO : 강의계획서 업데이트 기능?
}