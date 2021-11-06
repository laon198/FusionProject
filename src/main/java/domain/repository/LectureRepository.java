package domain.repository;

import domain.model.Lecture;

import java.util.List;

public interface LectureRepository {
    Lecture findByID(long id);
    void save(Lecture lecture);
    void remove(long lectureID);
    List<Lecture> findAll();
}
