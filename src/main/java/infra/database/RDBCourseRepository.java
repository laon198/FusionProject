package infra.database;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.option.Option;
import infra.option.course.CourseOption;
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

    public List<Course> findAll(){
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
    public List<Course> findByOption(CourseOption... options) {
        SqlSession session = null;
        List<Course> list = null;

//        StringBuilder query = new StringBuilder();
//        for(CourseOption option : options){
//            query.append(option.getQuery());
////            query.append(" AND ");
//        }

        try {
            session = sqlSessionFactory.openSession();
            session.selectList("mapper.CourseMapper.FindByOption");
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