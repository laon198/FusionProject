package domain.model;

public abstract class Member {
    protected Long id;
    protected String name;
    protected String department;

    protected Member(Long id, String name, String dep){
        this.id = id;
        this.name = name;
        this.department = dep;
    }
}
