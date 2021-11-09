package infra.mapper;

import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import infra.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LectureMapper {
    final String getAll = "SELECT * FROM LECTURES_TB";

    @Select(getAll)
    @Results(id = "lectureResultSet", value ={
            @Result(property = "id", column = "lecture_SQ"),
            @Result(property = "courseID", column = "course_id"),
            //TODO : lecturerID String 이여야하는데 지금 long 이다  바꿔야함.
//            @Result(property = "LecturerID", column = "professor_code"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "limit", column = "capacity"),
    })
    List<Lecture> getAll();

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "findAll")
//    @ResultMap("lectureResultSet")
    List<Map<String,Object>> findAll();

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insert")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(LectureDTO lectureDTO);

    @UpdateProvider(type = infra.mapper.sql.LectureSql.class, method = "update")
    @ResultMap("lectureResultSet")
    void updateLecture();




    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectLectureList")
    List<Map<String,Object>> selectLectureList();


    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectLectureTimes")
    List<Map<String,Object>> selectLectureTimes(long id);


    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectRegisterings")
    List<Map<String,Object>> selectRegisterings(long id);

}


