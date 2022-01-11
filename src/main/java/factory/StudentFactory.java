package factory;

import domain.model.Student;
import dto.StudentDTO;

public class StudentFactory {
    public Student create(StudentDTO dto){
        return Student.builder(
                dto.getName(),
                dto.getDepartment(),
                dto.getBirthDate(),
                dto.getStudentCode(),
                dto.getYear())
                .credit(dto.getCredit())
                .maxCredit(dto.getMaxCredit())
                .build();
    }
}
