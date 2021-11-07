package domain.model;

import java.time.LocalDate;

public abstract class Member {
    protected Long id;
    protected String name;
    protected String department;
    protected LocalDate birthDate;

    protected Member(Long id, String name, String dep, LocalDate birthDate){
        this.id = id;
        this.name = name;
        this.department = dep;
        this.birthDate = birthDate;
    }
}
