package infra.database.mapper;

import infra.database.option.lecture.LectureOption;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface LectureMapper {
    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findAll")
    List<Map<String, Object>> findAll();

    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findByOption")
    List<Map<String, Object>> findByOption(LectureOption... lectureOptions);


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
    int insert(Map<String, Object> lectureInfo);

//    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insert")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_PK")
//    int insert(@Param("courseID")long courseID, @Param("lectureCode")String lectureCode,
//               @Param("limit")int limit, @Param("applicant")int applicant,
//               @Param("professorCode")String professorCode);


    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insertLectureTime")
    void insertLectureTime(@Param("lectureID") long lectureID, @Param("lectureDay") String lectureDay,
                           @Param("room") String room, @Param("startTime") String startTime,
                           @Param("endTime") String endTime, @Param("lectureName") String lectureName);

    @InsertProvider(type = infra.database.mapper.sql.LectureSql.class, method = "insertPlanner")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "lecture_planner_PK")
    int insertLecturePlanner(Map<String, String> plannerInfo);


    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updateLecture")
    void updateLecture(@Param("courseID") long courseID, @Param("lectureCode") String lectureCode,
                       @Param("limit") int limit, @Param("applicant") int applicant,
                       @Param("professorCode") String professorCode, @Param("id") long id);

    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updateLectureTime")
    void updateLectureTime(@Param("id") long id, @Param("lectureDay") String lectureDay,
                           @Param("room") String room, @Param("startTime") String startTime,
                           @Param("endTime") String endTime, @Param("lectureName") String lectureName);


    @UpdateProvider(type = infra.database.mapper.sql.LectureSql.class, method = "updatePlanner")
    int updateLecturePlanner(Map<String, String> plannerInfo);


    @DeleteProvider(type = infra.database.mapper.sql.LectureSql.class, method = "delete")
    void deleteLecture(@Param("lectureID") long lectureID);

//    @SelectProvider(type = infra.database.mapper.sql.LectureSql.class, method = "findById")
//    @ResultMap("lectureResultSet")
//    Lecture findById(@Param("id")long id);

}


