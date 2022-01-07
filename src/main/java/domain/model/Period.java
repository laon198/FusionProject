package domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Period {
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    public Period(LocalDateTime beginTime, LocalDateTime endTime){
        setBeginTime(beginTime);
        setEndTime(endTime);
    }

    //해당시간이 기간안에 존재하는지 확인
    public boolean isInPeriod(LocalDateTime time) {
        return beginTime.isBefore(time) && endTime.isAfter(time);
    }

    //시작시간 설정
    private void setBeginTime(LocalDateTime time){
        if(!isValidBeginTime(time)){
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        beginTime = time;
    }

    //끝시간 설정
    private void setEndTime(LocalDateTime time){
        if(!isValidEndTime(time)){
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
        endTime = time;
    }

    //시작시간 유효성평가
    private boolean isValidBeginTime(LocalDateTime time){
        return endTime==null || !time.isAfter(endTime);
    }

    //끝시간 유효성평가
    private boolean isValidEndTime(LocalDateTime time){
        return beginTime==null || !time.isBefore(beginTime);
    }

    @Override
    public String toString() {
        return "Period{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Period)) return false;
        Period period = (Period) o;
        return Objects.equals(beginTime, period.beginTime) && Objects.equals(endTime, period.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginTime, endTime);
    }
}
