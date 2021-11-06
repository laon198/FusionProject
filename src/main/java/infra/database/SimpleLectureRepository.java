package infra.database;

import domain.model.Lecture;
<<<<<<< HEAD
=======
import domain.model.LectureID;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
import domain.repository.LectureRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleLectureRepository implements LectureRepository {
<<<<<<< HEAD
    private Map<Long, Lecture> repo = new HashMap<>();

    @Override
    public Lecture findByID(long id) {
=======
    private Map<LectureID, Lecture> repo = new HashMap<>();

    @Override
    public Lecture findByID(LectureID id) {
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
        return repo.get(id);
    }

    @Override
    public List<Lecture> findAll(){
        return new ArrayList<>(repo.values());
    }

    public void save(Lecture lecture){
        repo.put(lecture.getID(), lecture);
    }
<<<<<<< HEAD

    @Override
    public void remove(long lectureID) {
        repo.remove(lectureID);
    }
=======
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
}
