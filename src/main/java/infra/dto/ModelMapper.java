package infra.dto;

import domain.generic.LectureTime;
import domain.model.Course;
import domain.model.Lecture;
import domain.model.LecturePlanner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class ModelMapper {
    public static CourseDTO courseToDTO(Course course) throws NoSuchFieldException, IllegalAccessException {
        long id = getLongField(course, "id");
        int credit = getIntField(course, "credit");
        int targetYear = getIntField(course, "targetYear");
        String courseCode = getStringField(course, "courseCode");
        String department = getStringField(course, "department");
        String courseName = getStringField(course, "courseName");


        return new CourseDTO(
                id, targetYear, credit,
                courseCode, department, courseName
        );
    }

//    public static LectureDTO LectureToDTO(Lecture lecture) throws NoSuchFieldException, IllegalAccessException {
//        long id;
//        long courseID;
//        long lecturerID;
//        int limit;
//        Set<LectureTime> lectureTimes;
//        List<Long> registeredStudentIDs;
//        LecturePlanner planner;
//
//        return new LectureDTO(
//                courseID, lecturerID, limit, lectureTimes, registeredStudentIDs,planner
//        );
//    }


    private static String getStringField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (String)f1.get(obj);
    }

    public static int getIntField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (int)f1.get(obj);
    }

    public static long getLongField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (long)f1.get(obj);
    }
}
