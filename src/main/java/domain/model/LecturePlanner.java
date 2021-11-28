package domain.model;

import java.time.LocalDateTime;
import java.util.*;

public class LecturePlanner {
    private String goal;
    private String summary;
    private static Period writePeriod;

    public LecturePlanner(){
        //TODO : 기본값을 빈문자열로?
        writePeriod = new Period( //TODO : 기본 강의계획서 입력기간?
                LocalDateTime.of(2020,01,01,12,00),
                LocalDateTime.of(2021,12,21,12,00)
        );
    }

    public void writeItem(String itemName, String content) {
        if(!writePeriod.isInPeriod(LocalDateTime.now())){
            throw new IllegalStateException("강의계획서 입력기간이 아닙니다.");
        }

        if(itemName.equals("goal")){
            goal = content;
        }else if(itemName.equals("summary")){
            summary = content;
        }else{
            throw new IllegalArgumentException("존재하지않는 항목입니다.");
        }
    }

    public static void setWritingPeriod(Period period){
        writePeriod = period;
    }

    //TODO : 테스트용
    @Override
    public String toString() {
        return "LecturePlanner{" +
                '}';
    }
}
