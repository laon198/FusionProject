package application;

import domain.model.Student;
import domain.repository.StudentRepository;

import java.util.List;

public class StudentRetrieveAppService {
    private StudentRepository stdRepo;

    public StudentRetrieveAppService(
            StudentRepository stdRepo
    ){
        this.stdRepo = stdRepo;
    }

    public Student findByID(long id){
        return stdRepo.findByID(id);
    }

    public List<Student> findAll(){
        return stdRepo.findAll();
    }
}
