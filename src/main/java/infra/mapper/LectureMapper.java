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
            @Result(property = "id", column = "lecture_PK"),
            @Result(property = "courseID", column = "course_PK"),
            @Result(property = "lecturerID", column = "professor_code"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "limit", column = "capacity"),
    })
    List<Lecture> getAll();

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "findAll")
    List<Map<String,Object>> findAll();

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_PK")
    int insert(LectureDTO lectureDTO);

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insertLectureTime")
    void insertLectureTime(@Param("lectureID") long lectureID, @Param("lectureDay") String lectureDay,
                            @Param("room")String room, @Param("startTime") int startTime, @Param("endTime") int endTime);

    @InsertProvider(type = infra.mapper.sql.LectureSql.class, method = "insertPlanner")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_planner_PK")
    int insertLecturePlanner(Map<String, String> plannerInfo);

    @UpdateProvider(type = infra.mapper.sql.LectureSql.class, method = "update")
    @ResultMap("lectureResultSet")
    void updateLecture();

//    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "findById")
//    @ResultMap("lectureResultSet")
//    Lecture findById(@Param("id")long id);

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "findById")
    Map<String, Object> findByID(@Param("id")long id);

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectLectureList")
    List<Map<String,Object>> selectLectureList();

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectLectureTimes")
    List<Map<String,Object>> selectLectureTimes(long id);

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectRegisterings")
    List<Map<String,Object>> selectRegisterings(long id);

    @SelectProvider(type = infra.mapper.sql.LectureSql.class, method = "selectPlanner")
    Map<String, Object> selectPlanner(@Param("id")long id);
}


