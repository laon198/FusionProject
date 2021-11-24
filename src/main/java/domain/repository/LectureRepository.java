package domain.repository;

import domain.model.Lecture;
import infra.database.option.lecture.LectureOption;

import java.util.List;

public interface LectureRepository {
    Lecture findByID(long id);
    void save(Lecture lecture);
    void remove(Lecture lecture);
    void insert(Lecture lecture);
    List<Lecture> findByOption(LectureOption... options);
    List<Lecture> findAll();
}
