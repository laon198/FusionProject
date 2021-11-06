package domain.repository;

import domain.model.Lecture;
<<<<<<< HEAD
=======
import domain.model.LectureID;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd

import java.util.List;

public interface LectureRepository {
<<<<<<< HEAD
    Lecture findByID(long id);
    void save(Lecture lecture);
    void remove(long lectureID);
    List<Lecture> findAll();
=======
    Lecture findByID(LectureID id);
    List<Lecture> findAll();
    void save(Lecture lecture);
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
}
