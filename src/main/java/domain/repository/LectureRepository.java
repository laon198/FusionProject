package domain.repository;

import domain.model.Lecture;
import domain.model.LectureID;

import java.util.List;

public interface LectureRepository {
    Lecture findByID(LectureID id);
    List<Lecture> findAll();
    void save(Lecture lecture);
}
