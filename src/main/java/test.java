import domain.model.Student;
import domain.repository.StudentRepository;
import infra.database.RDBStudentRepository;

import java.time.LocalDate;
import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        StudentRepository stdRepo = new RDBStudentRepository();
        Student std = Student.builder()
                .id(2)
                .name("pro park")
                .department("SE")
                .studentCode("20180603")
                .birthDate("19990329")
                .year(2)
                .build();

        stdRepo.save(std);
    }
}
