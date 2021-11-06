package infra.dto;

import domain.model.Course;

import java.lang.reflect.Field;

public class ModelMapper {
    public static CourseDTO courseToDTO(Course course) throws NoSuchFieldException, IllegalAccessException {
        long id = getLongField(course, "id");
        int credit = getIntField(course, "credit");
//        int targetYear = getIntField(course, "targetYear");
//        String courseCode = getStringField(course, "courseCode");
//        String department = getStringField(course, "department");
//        String courseName = getStringField(course, "courseName");
        int targetYear = 2;
        String courseCode = "a";
        String department =  "b";
        String courseName = "c";

        return new CourseDTO(
                id, targetYear, credit,
                courseCode, department, courseName
        );
    }

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
