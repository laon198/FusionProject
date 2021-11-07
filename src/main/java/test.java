import domain.model.Student;
import domain.repository.StudentRepository;
import infra.database.RDBStudentRepository;

import java.util.List;

public class test {
    public static void main(String[] args) throws Exception {
        StudentRepository stdRepo = new RDBStudentRepository();
        List<Student> stdList = stdRepo.findAll();

//        System.out.println(stdList.size());
    }
}
