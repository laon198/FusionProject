package infra.dto;

import domain.generic.LectureTime;
import domain.model.Course;
import domain.model.Student;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ModelMapper {
    public static StudentDTO studentToDTO(Student std){
        try{
            long id = getSuperLongField(std, "id");
            String name = getSuperStringField(std, "name");
            String department = getSuperStringField(std, "department");
            String birthDate = getSuperStringField(std, "birthDate");
            String studentCode = getStringField(std, "studentCode");
            int credit = getIntField(std, "credit");
            int maxCredit = getIntField(std, "maxCredit");
            int year = getYearField(std, "year");

//        //TODO : 이렇게 받으면 DTO에서 학생객체에 변경을 가할 수 있어.
//        List<Long> registeredLectureIDs = getLongList(std, "registeredLectureIDs");
//        Set<LectureTime> timeTable = getTimeTable(std, "timeTable");

            return StudentDTO.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .birthDate(birthDate)
                    .credit(credit)
                    .studentCode(studentCode)
                    .maxCredit(maxCredit)
                    .year(year)
                    .build();
        }catch(NoSuchFieldException fieldException) {
            fieldException.getStackTrace();
        }catch(IllegalAccessException accessException){
            accessException.getStackTrace();
        }

        return null;
    }

    private static String getSuperStringField(Student obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (String)f1.get(obj);
    }

    private static long getSuperLongField(Student obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (long)f1.get(obj);
    }

    private static int getYearField(Student obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        if(f1.get(obj)==Student.Year.FRESHMAN){
            return 1;
        }else if(f1.get(obj)==Student.Year.SOPHOMORE){
            return 2;
        } else if(f1.get(obj)==Student.Year.JUNIOR){
            return 3;
        } else{
            return 4;
        }
    }


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

    private static List<Long> getLongList(Student obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (List<Long>)f1.get(obj);
    }

    private static Set<LectureTime> getTimeTable(Student obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (Set<LectureTime>)f1.get(obj);
    }

    private static LocalDate getLocalDateField(Student obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (LocalDate)f1.get(obj);
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
