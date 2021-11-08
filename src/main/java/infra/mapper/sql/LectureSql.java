package infra.mapper.sql;

import domain.model.Lecture;
import infra.dto.LectureDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.jdbc.SQL;

public class LectureSql {

    public String findAll(){
        SQL sql = new SQL(){{
            SELECT("*");
            FROM("lECTURES_TB");
        }};
        return sql.toString();
    }

    public String findByOption(){
        SQL sql = new SQL(){{}};
        return sql.toString();
    }


    public String insert() {
        SQL sql = new SQL() {{
            INSERT_INTO("LECTURES_TB");
            VALUES("course_SQ","1");
            VALUES("capacity","100");
            VALUES("applicant_CNT","0");
            VALUES("professor_code", "p1234");
            VALUES("lecture_code", "SE0004-01");
        }};
        return sql.toString();
    }

    public String update(){
        SQL sql = new SQL(){{
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
