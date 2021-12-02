package infra.database.mapper.sql;

import domain.model.Lecture;
import infra.database.option.lecture.LectureOption;
import org.apache.ibatis.jdbc.SQL;

public class LectureSql {

    // <SELECT>-------------------------------------------------------------------------------------------

    public String findAll() {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM("lECTURES_TB");
            INNER_JOIN("REGISTERINGS_TB ON REGISTERINGS_TB.lecture_PK = LECTURES_TB.lecture_PK");
            INNER_JOIN("LECTURE_TIMES_TB ON LECTURE_TIMES_TB.lecture_PK = LECTURES_TB.lecture_PK");
            INNER_JOIN("PROFESSORS_TB ON PROFESSORS_TB.professor_code = LECTURES_TB.professor_code");
        }};
        return sql.toString();
    }

    public String findById() {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM("lECTURES_TB");
            WHERE("lecture_PK = #{id}");
        }};
        return sql.toString();
    }

    public String findByOption(LectureOption... options){
        SQL sql = new SQL(){{
            SELECT("*");
            FROM("LECTURES_TB AS l");
            INNER_JOIN("COURSES_TB AS c ON c.course_PK = l.course_PK ");
            INNER_JOIN("PROFESSORS_TB AS p ON p.professor_code = l.professor_code");

        }};

        for(LectureOption option : options){
            sql.WHERE(option.getQuery());
        }

        return sql.toString();
    }

    public String selectLectureList() {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM("lECTURES_TB");
        }};
        return sql.toString();
    }

    public String selectLectureTimes(long id) {
        SQL sql = new SQL() {{
            SELECT("lecture_time_PK, start_period, end_period, day_of_week, lecture_room, lecture_name");
            FROM("lECTURE_TIMES_TB");
            INNER_JOIN("LECTURES_TB ON LECTURE_TIMES_TB.lecture_PK = LECTURES_TB.lecture_PK");
            WHERE("LECTURES_TB.lecture_PK = #{id}");
        }};
        return sql.toString();
    }

    public String selectRegisterings(long id) {
        SQL sql = new SQL() {{
            SELECT("registering_PK,student_code, LECTURES_TB.lecture_PK, register_date");
            FROM("REGISTERINGS_TB");
            INNER_JOIN("LECTURES_TB ON REGISTERINGS_TB.lecture_PK = LECTURES_TB.lecture_PK");
            WHERE("LECTURES_TB.lecture_PK = #{id}");
        }};
        return sql.toString();
    }

    public String selectPlanner() {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM("LECTURE_PLANNERS_TB");
            INNER_JOIN("LECTURES_TB ON LECTURE_PLANNERS_TB.lecture_PK = LECTURES_TB.lecture_PK");
            WHERE("LECTURES_TB.lecture_PK = #{id}");
        }};
        return sql.toString();
    }


    // </SELECT>-------------------------------------------------------------------------------------------

    // <INSERT>-------------------------------------------------------------------------------------------

    public String insertLectureTime() {
        SQL sql = new SQL() {{
            INSERT_INTO("LECTURE_TIMES_TB");
            VALUES("lecture_PK", "#{lectureID}");
            VALUES("start_period", "#{startTime}");
            VALUES("end_period", "#{endTime}");
            VALUES("day_of_week", "#{lectureDay}");
            VALUES("lecture_room", "#{room}");
            VALUES("lecture_name", "#{lectureName}");
        }};
        return sql.toString();
    }

    public String insert() {
        SQL sql = new SQL() {{
            INSERT_INTO("LECTURES_TB");
            VALUES("course_PK", "#{courseID}");
            VALUES("lecture_code", "#{lectureCode}");
            VALUES("capacity", "#{limit}");
            VALUES("applicant_CNT", "#{applicant}");
            VALUES("professor_code", "#{professorCode}");
        }};
        return sql.toString();
    }

    public String insertPlanner() {
        SQL sql = new SQL() {{
            INSERT_INTO("lecture_planners_tb");
            VALUES("lecture_goal", "#{goal}");
            VALUES("lecture_summary", "#{summary}");
            VALUES("lecture_PK" , "#{lecturePK}");
        }};
        return sql.toString();
    }

    // </INSERT>-------------------------------------------------------------------------------------------


    // <UPDATE>-------------------------------------------------------------------------------------------

    public String updateLectureTime() {
        SQL sql = new SQL() {{
            UPDATE("LECTURE_TIMES_TB");
            SET("start_period = #{startTime}");
            SET("end_period = #{endTime}");
            SET("day_of_week = #{lectureDay}");
            SET("lecture_room = #{room}");
            SET("lecture_name = #{lectureName}");
            WHERE("lecture_time_PK = #{id}");

        }};
        return sql.toString();
    }

    public String updatePlanner() {
        SQL sql = new SQL() {{
            UPDATE("lecture_planners_tb");
            SET("lecture_goal = #{goal}");
            SET("lecture_summary = #{summary}");
            WHERE("lecture_PK = #{id}");
        }};
        return sql.toString();
    }


    public String updateLecture() {
        SQL sql = new SQL() {{
            UPDATE("LECTURES_TB");
            SET("course_PK = #{courseID}");
            SET("lecture_code = #{lectureCode}");
            SET("capacity = #{limit}");
            SET("applicant_CNT = #{applicant}");
            SET("professor_code = #{professorCode}");
            WHERE("lecture_PK = #{id}");

        }};
        return sql.toString();
    }

    // <UPDATE>-------------------------------------------------------------------------------------------

    // <DELETE>-------------------------------------------------------------------------------------------

    public String delete() {
        SQL sql = new SQL() {{
            DELETE_FROM("LECTURES_TB");
            WHERE("lecture_PK = #{lectureID}");
        }};
        return sql.toString();
    }

    // </DELETE>-------------------------------------------------------------------------------------------
}
