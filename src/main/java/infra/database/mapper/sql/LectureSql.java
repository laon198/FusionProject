package infra.database.mapper.sql;

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

    public String selectLectureList() {
        SQL sql = new SQL() {{
            SELECT("*");
            FROM("lECTURES_TB");
        }};
        return sql.toString();
    }

    public String selectLectureTimes(long id) {
        SQL sql = new SQL() {{
            SELECT("start_period, end_period, day_of_week, lecture_room");
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
            VALUES("professor_code", "#{lecturerID}");
        }};
        return sql.toString();
    }

    public String insertPlanner() {
        SQL sql = new SQL() {{
            INSERT_INTO("lecture_planners_tb");
            VALUES("lecture_goal", "#{goal}");
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
            WHERE("lecture_time_PK = #{id}");

        }};
        return sql.toString();
    }

    public String updatePlanner() {
        SQL sql = new SQL() {{
            UPDATE("lecture_planners_tb");
            SET("lecture_goal = #{goal}");
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
            SET("professor_code = #{lecturerID}");
            WHERE("lecture_PK = #{id}");

        }};
        return sql.toString();
    }

    // <UPDATE>-------------------------------------------------------------------------------------------

    // <DELETE>-------------------------------------------------------------------------------------------

    //TODO lecture 삭제시, 연결된 lecture, lecture_time, registering, lecture_planner 삭제
    public String delete() {
        SQL sql = new SQL() {{
            DELETE_FROM("LECTURES_TB");
            WHERE("lecture_PK = #{lectureID}");
        }};
        return sql.toString();
    }

    // </DELETE>-------------------------------------------------------------------------------------------
}
