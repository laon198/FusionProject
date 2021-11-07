package infra.mapper;

import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

public interface LectureMapper {
    final String getAll = "SELECT * FROM LECTURES_TB";

    @Select(getAll)
    @Results(id = "lectureResultSet", value ={
            @Result(property = "id", column = "lecture_SQ"),
            @Result(property = "courseID", column = "course_id"),
            @Result(property = "LectureID", column = "lecture_id"),
            @Result(property = "limit", column = "max")
    })
    List<Lecture> getAll();
}
