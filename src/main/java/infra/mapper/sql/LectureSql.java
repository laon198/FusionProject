package infra.mapper.sql;

import org.apache.ibatis.jdbc.SQL;

public class LectureSql {

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

    public String selectPlanner(long id) {
        SQL sql = new SQL() {{
            SELECT("LECTURE_PK");
            FROM("LECTURE_PLANNERS_TB");
            INNER_JOIN("LECTURES_TB ON LECTURE_PLANNERS_TB.lecture_PK = LECTURES_TB.lecture_PK");
            WHERE("LECTURES_TB.lecture_PK = #{id}");
        }};
        return sql.toString();
    }


    public String findByOption() {
        SQL sql = new SQL() {{
        }};
        return sql.toString();
    }

    public String insertLectureTimes() {
        SQL sql = new SQL() {{
            INSERT_INTO("LECTURE_TIMES_TB");
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

    public String update() {
        SQL sql = new SQL() {{
            UPDATE("LECTURES_TB");
            SET("course_SQ = 1");
            SET("lecture_code =SE0004");
            SET("capacity = 60");
            SET("applicant_CNT = 1");
            SET("lecture_planner_SQ = 1");
            SET("professor_code = 11");
            WHERE("lecture_SQ = 12");
        }};
        return sql.toString();
    }

}
