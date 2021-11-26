package application;

import domain.model.LectureTime;
import domain.model.Lecture;
import domain.repository.LectureRepository;
import domain.service.LectureManageService;
import infra.dto.LectureDTO;
import infra.dto.LectureTimeDTO;
import infra.dto.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LectureAppService {
    private LectureRepository lectureRepo;

    public LectureAppService(LectureRepository lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    public void create(LectureDTO lectureDTO){
        LectureManageService m = new LectureManageService(
                lectureRepo
        );

        Set<LectureTime> times = new HashSet<>();
        for(LectureTimeDTO timeDTO : lectureDTO.getLectureTimes()){
            times.add(
                    LectureTime.builder()
                    .lectureDay(timeDTO.getLectureDay())
                    .endTime(timeDTO.getEndTime())
                    .startTime(timeDTO.getStartTime())
                    .room(timeDTO.getRoom())
                    .lectureName(timeDTO.getLectureName())
                    .build()
            );
        }

        Lecture lecture = m.create(
                lectureDTO.getCourseID(),
                lectureDTO.getLectureCode(),
                lectureDTO.getLecturerID(),
                lectureDTO.getLimit(),
                times
        );

        lectureRepo.insert(lecture);
    }

    public void delete(LectureDTO lectureDTO){
        lectureRepo.remove(Lecture.builder().id(lectureDTO.getId()).build());
    }

    public LectureDTO retrieveByID(long id){
        return ModelMapper.lectureToDTO(lectureRepo.findByID(id));
    }

    public List<LectureDTO> retrieveAll(){
        return lectureListToDTOList(lectureRepo.findAll());
    }

    private List<LectureDTO> lectureListToDTOList(List<Lecture> lectures){
        List<LectureDTO> dtos = new ArrayList<>();

        for(Lecture l : lectures){
            dtos.add(
                    ModelMapper.lectureToDTO(l)
            );
        }

        return dtos;
    }

//    public void writePlanner(LectureDTO lectureDTO, String writerCode){
//        Lecture lecture = lectureRepo.findByID(lectureDTO.getId());
//
//
//    }


    public void update(LectureDTO lectureDTO){
//        LectureManageService m = new LectureManageService(
//                lectureRepo
//        );
//
//        Set<LectureTime> times = new HashSet<>();
//        for(LectureTimeDTO timeDTO : lectureDTO.getLectureTimes()){
//            times.add(
//                    LectureTime.builder()
//                            .lectureDay(timeDTO.getLectureDay())
//                            .endTime(timeDTO.getEndTime())
//                            .startTime(timeDTO.getStartTime())
//                            .room(timeDTO.getRoom())
//                            .build()
//            );
//        }
//
//        Lecture lecture = Lecture.builder()
//                .id(lectureDTO.getId())
//                .lectureTimes(times)
//                .lectureCode(lectureDTO.getLectureCode())
//                .lecturerID(lectureDTO.getLecturerID())
//                .courseID(lectureDTO.getCourseID())
//                .limit(lectureDTO.getLimit())
//                .registerings(lectureDTO.getMyRegisterings())
//                .planner(lectureDTO.getPlanner())
//                .build();
//
//        m.update(lecture);
    }
}