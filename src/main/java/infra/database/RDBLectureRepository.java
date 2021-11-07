package infra.database;

import com.mysql.cj.Session;
import domain.model.Lecture;
import domain.repository.LectureRepository;
import infra.mapper.LectureMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

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

    }

    @Override
    public void remove(long lectureID) {

    }

    @Override
    public List<Lecture> findAll() {
        List<Lecture> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try{
            list = mapper.getAll();
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
