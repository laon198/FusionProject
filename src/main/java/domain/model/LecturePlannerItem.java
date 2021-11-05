package domain.model;

public class LecturePlannerItem {
    private String name;
    private String content;

    //TODO : 플래너아이템 생성 어디서?
    public LecturePlannerItem(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
