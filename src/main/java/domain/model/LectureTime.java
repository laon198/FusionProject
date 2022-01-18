package domain.model;

import java.util.Objects;

public class LectureTime {
    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI
    }

    public enum LecturePeriod {
        FIRST, SECOND, THIRD, FOURTH,
        FIFTH, SIXTH, SEVENTH, EIGHTH, NINTH;
    }

    private DayOfWeek lectureDay;
    private LecturePeriod startTime;
    private LecturePeriod endTime;
    private String room;
    private String lectureName;

    public static class Builder {
        private DayOfWeek lectureDay;
        private LecturePeriod startTime;
        private LecturePeriod endTime;
        private String room;
        private String lectureName;

        public Builder lectureDay(DayOfWeek value) {
            this.lectureDay = value;
            return this;
        }

        public Builder lectureName(String value) {
            lectureName = value;
            return this;
        }

        public Builder room(String value) {
            room = value;
            return this;
        }

        public Builder startTime(LecturePeriod value) {
            this.startTime = value;
            return this;
        }

        public Builder endTime(LecturePeriod value) {
            this.endTime = value;
            return this;
        }

        public LectureTime build() {
            return new LectureTime(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private LectureTime(Builder builder) {
        lectureDay = builder.lectureDay;
        startTime = builder.startTime;
        endTime = builder.endTime;
        room = builder.room;
        lectureName = builder.lectureName;
    }

    public LectureTime(DayOfWeek dayOfWeek, LecturePeriod startTime, LecturePeriod endTime, String room, String lectureName) {
        isValidTime(startTime, endTime);
        lectureDay = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.lectureName = lectureName;
    }

    //시작교시가 끝교시보다 작아야하는 유효성 체크
    private void isValidTime(LecturePeriod startTime, LecturePeriod endTime) {
        if (endTime.compareTo(startTime) < 0) {
            throw new IllegalArgumentException("시간설정 잘못됨");
        }
    }

    //겹치는 수강시간인지 확인
    public boolean isOverlappedTime(LectureTime time) {
        return lectureDay == time.lectureDay &&
                startTime.compareTo(time.endTime) <= 0 &&
                endTime.compareTo(time.startTime) >= 0;
    }

    public boolean isSameRoom(LectureTime time) {
        return room.equals(time.room);
    }

    @Override
    public String toString() {
        return "LectureTime{" +
                ", lectureDay=" + lectureDay +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", room='" + room + '\'' +
                ", lectureName='" + lectureName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTime that = (LectureTime) o;
        return lectureDay == that.lectureDay && startTime == that.startTime && endTime == that.endTime && Objects.equals(room, that.room) && Objects.equals(lectureName, that.lectureName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectureDay, startTime, endTime, room, lectureName);
    }
}
