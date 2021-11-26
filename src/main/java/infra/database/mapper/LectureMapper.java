package infra.database.mapper;

import domain.model.Lecture;
import infra.database.option.lecture.LectureOption;
import infra.dto.LectureDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface LectureMapper {
    final String getAll = "SELECT * FROM LECTURES_TB";

    @Select(getAll)
    @Results(id = "lectureResultSet", value = {
            @Result(property = "id", column = "lecture_PK"),
            @Result(property = "courseID", column = "course_PK"),
            @Result(property = "lecturerID", column = "professor_code"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "limit", column = "capacity"),
    })
    List<Lecture> getAll();

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findAll")
    List<Map<String, Object>> findAll();

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findByOption")
    List<Map<String, Object>> findByOption(LectureOption ...lectureOptions);


    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findById")
    Map<String, Object> findByID(@Param("id") long id);

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "selectLectureList")
    List<Map<String, Object>> selectLectureList();

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "selectLectureTimes")
    List<Map<String, Object>> selectLectureTimes(long id);

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "selectRegisterings")
    List<Map<String, Object>> selectRegisterings(long id);

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "selectPlanner")
    Map<String, Object> selectPlanner(@Param("id") long id);


    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_PK")
    int insert(LectureDTO lectureDTO);

    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insertLectureTime")
    void insertLectureTime(@Param("lectureID") long lectureID, @Param("lectureDay") String lectureDay,
                            @Param("room") String room, @Param("startTime") int startTime,
                                @Param("endTime") int endTime, @Param("lectureName") String lectureName);

    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insertPlanner")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_planner_PK")
    int insertLecturePlanner(Map<String, String> plannerInfo);


    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updateLecture")
    void updateLecture(LectureDTO lectureDTO);

    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updateLectureTime")
    void updateLectureTime(@Param("id") long id, @Param("lectureDay") String lectureDay,
                           @Param("room") String room, @Param("startTime") int startTime,
                            @Param("endTime") int endTime, @Param("lectureName")String lectureName);


    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updatePlanner")
    int updateLecturePlanner(Map<String, String> plannerInfo);


    @DeleteProvider(type = infra.database.mapper.sql.LectureSql.class, method = "delete")
    void deleteLecture(@Param("lectureID") long lectureID);

//    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findById")
//    @ResultMap("lectureResultSet")
//    Lecture findById(@Param("id")long id);

}


