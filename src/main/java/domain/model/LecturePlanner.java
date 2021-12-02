package domain.model;

import java.time.LocalDateTime;
import java.util.*;

public class LecturePlanner {
    private String goal;
    private String summary;

    public LecturePlanner(){
        goal = " ";
        summary = " ";
    }

    public void writeItem(String itemName, String content) {

        if(itemName.equals("goal")){
            goal = content;
        }else if(itemName.equals("summary")){
            summary = content;
        }else{
            throw new IllegalArgumentException("존재하지않는 항목입니다.");
        }
    }
}
