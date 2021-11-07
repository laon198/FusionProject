package infra.database;

import domain.model.Course;
import infra.dto.CourseDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class CourseDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public CourseDAO(SqlSessionFactory sqlSessionFactory)
    {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Course> ReadAll(){
        List<Course> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.CourseMapper.ReadAll");
        } finally{
            session.close();
        }
        return list;
    }

    public List<CourseDTO> ReadTargetGrade(int grade){
        List<CourseDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.CourseMapper.ReadTargetGrade", grade);
        } finally{
            session.close();
        }
        return list;
    }

    public void UpdateCourseName(CourseDTO courseDTO){
        SqlSession session = null;
        try{
            session = sqlSessionFactory.openSession();
            session.update("mapper.CourseMapper.UpdateCourseName", courseDTO);
            session.commit();
        }finally {
            session.close();
        }
    }

    public void CreateCourse(CourseDTO courseDTO) {
        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(true);
            session.insert("mapper.CourseMapper.CreateCourse", courseDTO);
            session.commit();
        } finally {
            session.close();
        }
    }
}
