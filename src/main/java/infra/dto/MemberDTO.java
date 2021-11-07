package infra.dto;

import java.time.LocalDate;

public class MemberDTO {
    protected Long id;
    protected String name;
    protected String department;
    protected LocalDate birthDate;

    protected MemberDTO(Long id, String name, String dep, LocalDate birthDate){
        this.id = id;
        this.name = name;
        this.department = dep;
        this.birthDate = birthDate;
    }
}
