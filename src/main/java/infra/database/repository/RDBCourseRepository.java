package infra.database.repository;

import domain.model.Course;
import domain.repository.CourseRepository;
import infra.database.MyBatisConnectionFactory;
import infra.database.option.course.CourseOption;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class RDBCourseRepository implements CourseRepository {
    private SqlSessionFactory sqlSessionFactory;

    public RDBCourseRepository( SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<Course> findAll(){
        SqlSession session = null;
        List<Course> list = null;

        try {
            session = sqlSessionFactory.openSession();
            list = session.selectList("mapper.CourseMapper.ReadAll");
            session.commit();

            if(list.size()==0){
                throw new IllegalArgumentException("해당하는 결과가 없습니다.");
            }
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
            list = session.selectList("mapper.CourseMapper.FindByOption");
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
        SqlSession session = null;
        Course course = null;
        try {
            session = sqlSessionFactory.openSession();
            course = session.selectOne("mapper.CourseMapper.findByID", id);
            session.commit();
            if(course==null){
                throw new IllegalArgumentException("해당하는 결과가 없습니다.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return course;
    }

    @Override
    public List<Course> findByYear(int year) {
        SqlSession session = null;
        List<Course> list = null;
        try {
            session = sqlSessionFactory.openSession();
            list = session.selectList("mapper.CourseMapper.ReadTargetGrade",year);
            session.commit();
            if(list.size()==0){
                throw new IllegalArgumentException("해당하는 결과가 없습니다.");
            }
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
        if(course.getId()==-1){
            insert(course);
        }else{
            update(course);
        }
    }

    private void update(Course course){
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

    private void insert(Course course) {
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

    @Override
    public void remove(Course course) {
        SqlSession session = null;

        try{
            session = sqlSessionFactory.openSession();
            session.delete("mapper.CourseMapper.RemoveCourse", course.getId());
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
