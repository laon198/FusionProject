package domain.generic;

import java.util.Objects;

public class LectureTime {
    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI
    }
    public enum LecturePeriod {
        FIRST, SECOND, THIRD, FOURTH,
        FIFTH, SIXTH, SEVENTH, EIGHTH, NINTH
    }

    private DayOfWeek lectureDay;
    private LecturePeriod startTime;
    private LecturePeriod endTime;
    private String room;

    public LectureTime(DayOfWeek dayOfWeek, LecturePeriod startTime, LecturePeriod endTime, String room){
        isValidTime(startTime, endTime);
        lectureDay = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    private void isValidTime(LecturePeriod startTime, LecturePeriod endTime){
        if(endTime.compareTo(startTime) < 0){
            throw new IllegalArgumentException("시간설정 잘못됨");
        }
    }

    public boolean isOverlappedTime(LectureTime time){
        return lectureDay==time.lectureDay &&
                startTime.compareTo(time.endTime)<=0 &&
                endTime.compareTo(time.startTime)>=0;
    }

    public boolean isSameRoom(LectureTime time){
<<<<<<< HEAD
        return room.equals(time.room);
    }

    public LectureTime setRoom(String room) {
        return new LectureTime(
                this.lectureDay,
                this.startTime,
                this.endTime,
                room
        );
=======
        return time.room==room;
>>>>>>> be40034d7922ccc897f017a67b80ed559b184bcd
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTime that = (LectureTime) o;
        return lectureDay == that.lectureDay && startTime == that.startTime
                && endTime == that.endTime && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectureDay, startTime, endTime, room);
    }
}
