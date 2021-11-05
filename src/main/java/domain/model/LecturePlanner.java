package domain.model;

import domain.generic.Period;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class LecturePlanner {
    private Set<LecturePlannerItem> items;
    private static Period writePeriod;

    public LecturePlanner(){
        items = new HashSet<>();
        items.add(new LecturePlannerItem("bookName")); //TODO : 기본항목변경필요
        writePeriod = new Period( //TODO : 기본 강의계획서 입력기간?
                LocalDateTime.of(2020,01,01,12,00),
                LocalDateTime.of(2021,12,21,12,00)
        );
    }

    public void setItem(String itemName, String content) {
        if(!writePeriod.isInPeriod(LocalDateTime.now())){
            throw new IllegalStateException("강의계획서 입력기간이 아닙니다.");
        }

        for(LecturePlannerItem item : items){
            if(itemName.equals(item.getName())){
                item.setContent(content);
                return;
            }
        }

        throw new IllegalArgumentException("존재하지않는 항목입니다.");
    }

    public static void setWritingPeriod(Period period){
        writePeriod = period;
    }
}
