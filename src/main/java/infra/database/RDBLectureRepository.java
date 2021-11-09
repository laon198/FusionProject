package infra.database;

import domain.generic.LectureTime;
import domain.model.Lecture;
import domain.model.LecturePlanner;
import domain.model.Registering;
import domain.repository.LectureRepository;
import infra.dto.LectureDTO;
import infra.dto.LectureTimeDTO;
import infra.dto.ModelMapper;
import infra.mapper.LectureMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

public class RDBLectureRepository implements LectureRepository {
    private SqlSessionFactory sqlSessionFactory = null;

    public RDBLectureRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @Override
    public Lecture findByID(long id) {
        return null;
    }

    @Override
    public void save(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {
            mapper.updateLecture();
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    public void insert(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        LectureDTO lectureDTO = ModelMapper.lectureToDTO(lecture);
        Set<LectureTimeDTO> lectureTimes = lectureDTO.getLectureTimes();
        try {
            mapper.insert(lectureDTO);
            mapper.insertLectureTimes(lectureTimes);
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(long lectureID) {

    }


    @Override
    public List<Lecture> findAll() {
        List<Lecture> list = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {
            // 개설교과목 목록 조회
            List<Map<String, Object>> lectureList = mapper.selectLectureList();

            //각 개설교과목에 해당하는 강의시간 객채만들어서 할당
            for (Map map : lectureList) {

                // Lecture 생성에 필요한 맴버들
                System.out.println("map.toString() = " + map.toString());
                long id = (long)map.get("lecture_PK");
                long courseID = (long)map.get("course_PK");
                String lectureCode = map.get("lecture_code").toString();
                String lecturerID = map.get("professor_code").toString();
                int limit = (int)map.get("capacity");


                // registerings 에 registering 추가
                Set<Registering> registerings = new HashSet<>();
                List<Map<String, Object>> registers = mapper.selectRegisterings((long) map.get("lecture_PK"));
                for (Map register : registers) {
                    System.out.println("register.toString() = " + register.toString());
                    Registering registering = Registering.builder()
                            .id((long)register.get("registering_PK"))
                            .studentCode(register.get("student_code").toString())
                            .registeringTime(register.get("register_date").toString())
                            .lectureID((long)register.get("lecture_PK"))
                            .build();
                    registerings.add(registering);
                }

                //lectureTimes 에 lectureTime 추가
                Set<LectureTime> lectureTimes = new HashSet<>();
                List<Map<String, Object>> times = mapper.selectLectureTimes((long) map.get("lecture_PK"));
                for (Map time : times) {
                    System.out.println("time.toString() = " + time.toString());
                    LectureTime lectureTime = LectureTime.builder()
                            .lectureDay(time.get("day_of_week").toString())
                            .startTime((int) time.get("start_period"))
                            .endTime((int) time.get("end_period"))
                            .room(time.get("lecture_room").toString())
                            .build();
                    lectureTimes.add(lectureTime);

                }

                LecturePlanner planner = new LecturePlanner();
                // Lecture 생성해서 리턴할 List에 추가
                list.add(new Lecture(id,lecturerID,limit,courseID,lectureCode,lectureTimes,registerings));
            }
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;

    }

//    @Override
//    public List<Lecture> findAll() {
//        List<Lecture> list = null;
//        List<Map<String,Object>> tmpList = null;
//        SqlSession session = sqlSessionFactory.openSession();
//        LectureMapper mapper = session.getMapper(LectureMapper.class);
//        try{
//            List<Map<String,Object>> lectureList = mapper.selectLectureList();
//            for(Map map : lectureList){
//            Set<LectureTime> lectureTimes = null;
//            List<Map<String, Object>> times = mapper.selectLectureTimes((long) map.get("lecture_PK"));
//            for(Map time : times){
//                System.out.println("time.toString() = " + time.toString());
//                LectureTime lectureTime = new LectureTime(time.get("day_of_week"),time.get("start_period"),time.get("end_period"),time.get("lecture_room").toString());
//            }
//            }
//
//
//
//
//
//
//
//            tmpList = mapper.findAll();
//            for(Map map : tmpList){
//                System.out.println("map.keySet() = " + map.keySet());
//                System.out.println("map.toString() = " + map.toString());
//            }
//            LecturePlanner planner = null;
//
//            session.commit();
//
//        }catch (Exception e){
//            e.printStackTrace();
//            session.rollback();
//        }finally{
//            session.close();
//        }
//
//        return list;
//    }
}
