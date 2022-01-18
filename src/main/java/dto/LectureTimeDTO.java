package dto;

import domain.model.LectureTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LectureTimeDTO {
    private long id;
    private LectureTime.DayOfWeek lectureDay;
    private LectureTime.LecturePeriod startTime;
    private LectureTime.LecturePeriod endTime;
    private String room;
    private String lectureName;

    public static class Builder{
        private long id;
        private LectureTime.DayOfWeek lectureDay;
        private LectureTime.LecturePeriod startTime;
        private LectureTime.LecturePeriod endTime;
        private String room;
        private String lectureName;

        public Builder id(long value){
            id = value;
            return this;
        }

        public Builder lectureName(String value){
            lectureName = value;
            return this;
        }

        public Builder room(String value){
            room = value;
            return this;
        }

        public Builder lectureDay(LectureTime.DayOfWeek value){
            lectureDay = value;
            return this;
        }

        public Builder startTime(LectureTime.LecturePeriod value){
            startTime = value;
            return this;
        }

        public Builder endTime(LectureTime.LecturePeriod value){
            endTime = value;
            return this;
        }


        public LectureTimeDTO build(){
            return new LectureTimeDTO(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public LectureTimeDTO(){}

    private LectureTimeDTO(Builder builder){
        id = builder.id;
        lectureDay = builder.lectureDay;
        startTime = builder.startTime;
        endTime = builder.endTime;
        room = builder.room;
        lectureName = builder.lectureName;
    }
}
