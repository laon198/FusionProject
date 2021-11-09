package infra.database;

import com.mysql.cj.Session;
import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import domain.repository.LectureRepository;
import infra.mapper.LectureMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RDBLectureRepository implements LectureRepository {
    private SqlSessionFactory sqlSessionFactory = null;
    public RDBLectureRepository(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @Override
    public Lecture findByID(long id) {
        return null;
    }

    @Override
    public void save(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try{
            mapper.updateLecture();
            session.commit();

        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally{
            session.close();
        }
    }

    public void insert(Lecture lecture){
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try{
            mapper.insert(lecture);
            session.commit();

        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally{
            session.close();
        }
    }

    @Override
    public void remove(long lectureID) {

    }

    @Override
    public List<Lecture> findAll() {
        List<Lecture> list = null;
        List<Map<String,Object>> tmpList = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try{
            List<Map<String,Object>> lectureList = mapper.selectLectureList();
            for(Map map : lectureList){
            Set<LectureTime> lectureTimes = null;
            List<Map<String, Object>> times = mapper.selectLectureTimes((long) map.get("lecture_PK"));
            for(Map time : times){
                System.out.println("time.toString() = " + time.toString());
                LectureTime lectureTime = new LectureTime(time.get("day_of_week"),time.get("start_period"),time.get("end_period"),time.get("lecture_room").toString());
            }
            }







            tmpList = mapper.findAll();
            for(Map map : tmpList){
                System.out.println("map.keySet() = " + map.keySet());
                System.out.println("map.toString() = " + map.toString());
            }
            LecturePlanner planner = null;

            session.commit();

        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally{
            session.close();
        }

        return list;
    }
}
