package infra.database;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.option.Option;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

public class RDBCourseRepository implements CourseRepository {

    private SqlSessionFactory sqlSessionFactory = null;

    public RDBCourseRepository(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    public List<Course> findAll() {
        SqlSession session = null;
        List<Course> list = null;
        try {
            session = sqlSessionFactory.openSession();
            session.selectList("mapper.CourseMapper.ReadAll");
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }


    @Override
    public List<Course> findByOption(Option... option) {
        SqlSession session = null;
        List<Course> list = null;
        try {
            session = sqlSessionFactory.openSession();
            session.selectList("mapper.CourseMapper.FindByOption",option);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }


    @Override
    public Course findByID(long id) {
        return null;
    }

    @Override
    public List<Course> findByYear(int year) {
        SqlSession session = null;
        List<Course> list = null;
        try {
            session = sqlSessionFactory.openSession();
            session.selectList("mapper.CourseMapper.ReadTargetGrade",year);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }

    @Override
    public void save(Course course) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.update("mapper.CourseMapper.UpdateCourse", course);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public void insert(Course course) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession();
            session.insert("mapper.CourseMapper.CreateCourse", course);
            session.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }
}
