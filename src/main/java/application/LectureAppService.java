package application;

import domain.repository.LectureRepository;
import infra.dto.LectureDTO;

public class LectureAppService {
    private LectureRepository lectureRepo;

    public LectureAppService(LectureRepository lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    public void create(LectureDTO lectureDTO){
    }
}