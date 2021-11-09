package infra.database;

import domain.model.Professor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessorRepoTest {
    static RDBProfessorRepository profRepo;

    @BeforeAll
    public static void setup(){
        profRepo = new RDBProfessorRepository();
    }

//    @DisplayName("교수 조회 테스트")
//    @Test
//    public void findTest(){
//
//    }

    @DisplayName("교수 전체 조회 테스트")
    @Test
    public void findAllTest(){
        List<Professor> list = profRepo.findAll();
        assertEquals(2, list.size());
    }

    @DisplayName("교수 save테스트-insert")
    @Test
    public void saveTest(){
        Professor p1 = Professor.builder()
                .id(12)
                .name("park")
                .department("SE")
                .birthDate("19801112")
                .professorCode("P1236")
                .build();
        profRepo.save(p1);
    }
}
