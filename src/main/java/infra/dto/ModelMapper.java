package infra.dto;

import domain.generic.LectureTime;
import domain.model.*;

import java.lang.reflect.Field;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelMapper {

    public static AdminDTO adminToDTO(Admin admin){
        try{
            long id = getSuperLongField(admin, "id");
            String name = getSuperStringField(admin, "name");
            String department = getSuperStringField(admin, "department");
            String birthDate = getSuperStringField(admin, "birthDate");
            String adminCode = getStringField(admin, "admin_code");

            return AdminDTO.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .birthDate(birthDate)
                    .adminCode(adminCode)
                    .build();

        }catch(NoSuchFieldException | IllegalAccessException e){
            e.getStackTrace();
        }

        return null;
    }

    public static ProfessorDTO professorToDTO(Professor prof){
        try{
            long id = getSuperLongField(prof, "id");
            String name = getSuperStringField(prof, "name");
            String department = getSuperStringField(prof, "department");
            String birthDate = getSuperStringField(prof, "birthDate");
            String professorCode = getStringField(prof, "professorCode");
            Set<LectureTimeDTO> timeTable = getLectureTimeDTOTable(getTimeTable(prof, "timeTable"));
            //TODO : timeTable

            return ProfessorDTO.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .birthDate(birthDate)
                    .professorCode(professorCode)
                    .build();

        }catch(NoSuchFieldException | IllegalAccessException e){
            e.getStackTrace();
        }

        return null;
    }

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
            List<Long> registeredLectureIDs = getLongList(std, "registeredLectureIDs");
            Set<LectureTimeDTO> timeTable = getLectureTimeDTOTable(getTimeTable(std, "timeTable"));

            return StudentDTO.builder()
                    .id(id)
                    .name(name)
                    .department(department)
                    .birthDate(birthDate)
                    .credit(credit)
                    .studentCode(studentCode)
                    .maxCredit(maxCredit)
                    .year(year)
                    .registeredLectureIDs(registeredLectureIDs)
                    .timeTable(timeTable)
                    .build();
        }catch(NoSuchFieldException | IllegalAccessException e) {
            e.getStackTrace();
        }

        return null;
    }

    private static Set<LectureTimeDTO> getLectureTimeDTOTable(Set<LectureTime> timeTable) throws NoSuchFieldException, IllegalAccessException {
        Set<LectureTimeDTO> dtoList = new HashSet<>();

        for(LectureTime time : timeTable){
            String room = getStringField(time, "room");
            LectureTime.DayOfWeek lectureDay = getLectureDay(time, "lectureDay");
            LectureTime.LecturePeriod startTime = getLecturePeriod(time, "startPeriod");
            LectureTime.LecturePeriod endTime = getLecturePeriod(time, "endTime");

            dtoList.add(
                    LectureTimeDTO.builder()
                        .room(room)
                        .lectureDay(lectureDay)
                        .startTime(startTime)
                        .endTime(endTime)
                        .build()
            );
        }

        return dtoList;
    }

    private static LectureTime.DayOfWeek getLectureDay(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (LectureTime.DayOfWeek) f1.get(obj);
    }

    private static LectureTime.LecturePeriod getLecturePeriod(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (LectureTime.LecturePeriod) f1.get(obj);
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

    private static String getSuperStringField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (String)f1.get(obj);
    }

    private static long getSuperLongField(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getSuperclass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (long)f1.get(obj);
    }

    private static int getYearField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
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

    private static List<Long> getLongList(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (List<Long>)f1.get(obj);
    }

    private static Set<LectureTime> getTimeTable(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f1 = obj.getClass().getDeclaredField(fieldName);
        f1.setAccessible(true);
        return (Set<LectureTime>)f1.get(obj);
    }

    private static LocalDate getLocalDateField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
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
