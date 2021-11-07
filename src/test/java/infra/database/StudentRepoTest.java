package infra.database;

import domain.model.Student;
import domain.repository.StudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentRepoTest {
    static StudentRepository stdRepo;

    @BeforeAll
    public static void setup(){
        stdRepo = new RDBStudentRepository();
    }

    @DisplayName("학생 조회 테스트")
    @Test
    public void selectTest(){
        Student std = stdRepo.findByID(1);

        assertEquals(1, std.getID());
    }

//    @DisplayName("전체 학생 조회 테스트")
//    @Test
//    public void selectAllTest(){
//        List<Student> stdList = stdRepo.findAll();
//
//        //TODO : 테스트어려움
//        assertEquals(2, stdList.size());
//    }

    @DisplayName("학생 저장 테스트")
    @Test
    public void saveTest(){
        Student std = Student.builder()
                    .id(5)
                .name("pro park")
                .department("SE")
                .birthDate("19991112")
                .year(2)
                .build();

        stdRepo.save(std);
    }
}
