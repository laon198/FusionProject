package infra.mapper;

import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import infra.dto.LectureDTO;
import infra.dto.LectureTimeDTO;
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
            @Result(property = "LecturerID", column = "professor_code"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "limit", column = "capacity"),
    })
    List<Lecture> getAll();

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "findAll")
    List<Map<String,Object>> findAll();

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insert")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(LectureDTO lectureDTO);

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insertLectureTimes")
    void insertLectureTimes(Set<LectureTimeDTO> lectureTimes);

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


