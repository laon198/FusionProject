package infra.database.repository;

import domain.model.Lecture;
import domain.model.LecturePlanner;
import domain.model.LectureTime;
import domain.model.Registering;
import domain.repository.CourseRepository;
import domain.repository.LectureRepository;
import domain.repository.ProfessorRepository;
import infra.database.mapper.LectureMapper;
import infra.database.option.lecture.LectureOption;
import infra.database.option.professor.ProfessorCodeOption;
import dto.LectureDTO;
import dto.LectureTimeDTO;
import dto.ModelMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

public class RDBLectureRepository implements LectureRepository {
    private SqlSessionFactory sqlSessionFactory;
    private CourseRepository courseRepo;
    private ProfessorRepository profRepo;

    public RDBLectureRepository(
            CourseRepository courseRepo, ProfessorRepository profRepo,
            SqlSessionFactory sqlSessionFactory) {
        this.courseRepo = courseRepo;
        this.profRepo = profRepo;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<Lecture> findByOption(LectureOption... lectureOptions) {
        List<Lecture> list = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {
            List<Map<String, Object>> lectureList = mapper.findByOption(lectureOptions);

            //각 개설교과목에 해당하는 강의시간 객채만들어서 할당
            for (Map<String, Object> map : lectureList) {
                long id = (long) map.get("lecture_PK");

                // registerings 에 registering 추가
                Set<Registering> registerings = getRegSetFrom(
                        mapper.selectRegisterings((long) map.get("lecture_PK"))
                );

                //lectureTimes 에 lectureTime 추가
                Set<LectureTime> lectureTimes = getLtimeFrom(
                        mapper.selectLectureTimes((long) map.get("lecture_PK"))
                );

                Map<String, Object> plannerInfo = mapper.selectPlanner(id);
                LecturePlanner planner = new LecturePlanner();
                planner.writeItem("goal", plannerInfo.get("lecture_goal").toString());
                planner.writeItem("summary", plannerInfo.get("lecture_summary").toString());

                // Lecture 생성해서 리턴할 List에 추가
                list.add(
                        mapToLecture(map, lectureTimes, registerings, planner)
                );
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

    @Override
    public Lecture findByID(long id) {
        Lecture lecture = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        try {

            Set<Registering> registerings = getRegSetFrom(mapper.selectRegisterings(id));
            Set<LectureTime> lectureTimes = getLtimeFrom(mapper.selectLectureTimes(id));

            Map<String, Object> plannerInfo = mapper.selectPlanner(id);
            LecturePlanner planner = new LecturePlanner();
            planner.writeItem("goal", plannerInfo.get("lecture_goal").toString());
            planner.writeItem("summary", plannerInfo.get("lecture_summary").toString());

            lecture = mapToLecture(mapper.findByID(id), lectureTimes, registerings, planner);

            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return lecture;
    }

    @Override
    public void save(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        LectureDTO lectureDTO = ModelMapper.lectureToDTO(lecture);
        LectureTimeDTO[] lectureTimes = lectureDTO.getLectureTimes();

        try {
            Map<String, String> items = new HashMap<>();
            items.put("goal", lectureDTO.getPlanner().getGoal());
            items.put("summary", lectureDTO.getPlanner().getSummary());

            items.put("id", Long.toString(lectureDTO.getId()));
            mapper.updateLecturePlanner(items);
            mapper.updateLecture(
                    lectureDTO.getCourseID(), lectureDTO.getLectureCode(),
                    lectureDTO.getLimit(), lectureDTO.getApplicant(),
                    lectureDTO.getProfessorCode(), lectureDTO.getId()
            );

            for (LectureTimeDTO lectureTimeDTO : lectureTimes) {
                if (lectureTimeDTO.getId() == -1) {
                    mapper.insertLectureTime(lectureDTO.getId(), lectureTimeDTO.getLectureDay().toString(),
                            lectureTimeDTO.getRoom(), lectureTimeDTO.getStartTime().toString(),
                            lectureTimeDTO.getEndTime().toString(), lectureTimeDTO.getLectureName());
                } else {
                    mapper.updateLectureTime(
                            lectureTimeDTO.getId(),
                            lectureTimeDTO.getLectureDay().toString(),
                            lectureTimeDTO.getRoom(), lectureTimeDTO.getStartTime().toString(),
                            lectureTimeDTO.getEndTime().toString(), lectureTimeDTO.getLectureName()
                    );
                }
            }

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
        LectureTimeDTO[] lectureTimes = lectureDTO.getLectureTimes();
        Map<String, Object> lectureInfo = new HashMap<>();
        lectureInfo.put("courseID", lectureDTO.getCourseID());
        lectureInfo.put("lectureCode", lectureDTO.getLectureCode());
        lectureInfo.put("limit", lectureDTO.getLimit());
        lectureInfo.put("applicant", lectureDTO.getApplicant());
        lectureInfo.put("professorCode", lectureDTO.getProfessorCode());
        lectureInfo.put("id", "");
        try {
            mapper.insert(lectureInfo);

            Map<String, String> items = new HashMap<>();
            items.put("goal", lectureDTO.getPlanner().getGoal());
            items.put("summary", lectureDTO.getPlanner().getSummary());
            items.put("id", "");
            items.put("lecturePK", (String) lectureInfo.get("id"));
            mapper.insertLecturePlanner(items);

            for (LectureTimeDTO lectureTimeDTO : lectureTimes) {
                mapper.insertLectureTime(
                        Long.parseLong((String) lectureInfo.get("id")), lectureTimeDTO.getLectureDay().toString(),
                        lectureTimeDTO.getRoom(), lectureTimeDTO.getStartTime().toString(),
                        lectureTimeDTO.getEndTime().toString(), lectureTimeDTO.getLectureName()
                );
            }

            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(Lecture lecture) {
        SqlSession session = sqlSessionFactory.openSession();
        LectureMapper mapper = session.getMapper(LectureMapper.class);
        LectureDTO lectureDTO = ModelMapper.lectureToDTO(lecture);
        long lectureID = lectureDTO.getId();
        try {
            mapper.deleteLecture(lectureID);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
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
                long id = (long) map.get("lecture_PK");

                // registerings 에 registering 추가
                Set<Registering> registerings = getRegSetFrom(
                        mapper.selectRegisterings((long) map.get("lecture_PK"))
                );

                //lectureTimes 에 lectureTime 추가
                Set<LectureTime> lectureTimes = getLtimeFrom(
                        mapper.selectLectureTimes((long) map.get("lecture_PK"))
                );

                Map<String, Object> plannerInfo = mapper.selectPlanner(id);
                LecturePlanner planner = new LecturePlanner();
                planner.writeItem("goal", plannerInfo.get("lecture_goal").toString());
                planner.writeItem("summary", plannerInfo.get("lecture_summary").toString());

                // Lecture 생성해서 리턴할 List에 추가
                list.add(
                        mapToLecture(map, lectureTimes, registerings, planner)
                );
            }
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        if(list.size()==0){
            throw new IllegalArgumentException("해당하는 결과가 없습니다.");
        }

        return list;
    }

    private Set<Registering> getRegSetFrom(List<Map<String, Object>> regs) {
        Set<Registering> registerings = new HashSet<>();
        for (Map<String, Object> reg : regs) {
            registerings.add(
                    Registering.builder(
                            (long) reg.get("lecture_PK"),
                            reg.get("student_code").toString(),
                            reg.get("register_date").toString()
                            )
                            .id((long) reg.get("registering_PK"))
                            .build()
            );
        }

        return registerings;
    }

    private Set<LectureTime> getLtimeFrom(List<Map<String, Object>> times) {
        Set<LectureTime> lectureTimes = new HashSet<>();
        for (Map time : times) {
            lectureTimes.add(
                    LectureTime.builder()
                            .lectureDay(LectureTime.DayOfWeek.valueOf(time.get("day_of_week").toString()))
                            .startTime(LectureTime.LecturePeriod.valueOf((String)time.get("start_period")))
                            .endTime(LectureTime.LecturePeriod.valueOf((String)time.get("end_period")))
                            .lectureName((String)time.get("lecture_name"))
                            .room(time.get("lecture_room").toString())
                            .build()
            );
        }
        return lectureTimes;
    }

    private Lecture mapToLecture(Map<String, Object> lectureMap, Set<LectureTime> times,
                                 Set<Registering> regs, LecturePlanner planner) {
        return Lecture.builder()
                .id((long) lectureMap.get("lecture_PK"))
                .courseID((long) lectureMap.get("course_PK"))
                .lectureCode(lectureMap.get("lecture_code").toString())
                .professorID((long)lectureMap.get("professor_code"))
                .limit((int) lectureMap.get("capacity"))
                .registerings(regs)
                .lectureTimes(times)
                .planner(planner)
                .build();

    }
}
