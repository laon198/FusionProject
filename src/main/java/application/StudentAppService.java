package application;

import domain.model.Account;
import domain.model.Registering;
import domain.model.Student;
import domain.repository.AccountRepository;
import domain.repository.RegisteringRepository;
import domain.repository.StudentRepository;
import factory.AccountFactory;
import factory.StudentFactory;
import infra.database.option.registering.LectureIDOption;
import infra.database.option.student.StudentCodeOption;
import infra.database.option.student.StudentOption;
import dto.LectureDTO;
import dto.ModelMapper;
import dto.StudentDTO;

import java.util.List;

//학생과 관련된 기능을 수행하는 객체
public class StudentAppService {
    private final StudentRepository stdRepo;
    private final AccountRepository accRepo;
    private final RegisteringRepository regRepo;
    private final StudentFactory stdFactory;
    private final AccountFactory accountFactory;

    public StudentAppService(
            StudentRepository stdRepo, AccountRepository accRepo,
            RegisteringRepository regRepo) {
        this.stdRepo = stdRepo;
        this.accRepo = accRepo;
        this.regRepo = regRepo;
        stdFactory = new StudentFactory();
        accountFactory = new AccountFactory();
    }

    //학생 생성 기능
    public void create(StudentDTO stdDTO) {
        //받은 정보로 학생객체 생성
        Student std = stdFactory.create(stdDTO);

        //생성한 학생 객체 데이터베이스에 저장
        long stdID = stdRepo.save(std);

        //학생객체에 대한 계정 생성
        Account acc = accountFactory.create(stdDTO, stdID, "STUD");

        //생성한 계정 객체 데이터베이스에 저장
        accRepo.save(acc);
    }

    //학생 수정 기능
    public void update(StudentDTO stdDTO) throws IllegalArgumentException {
        Student std = stdRepo.findByID(stdDTO.getId());
        std.setName(stdDTO.getName());

        stdRepo.save(std);
    }

    //학생 삭제 기능
    public void delete(StudentDTO stdDTO) {
        Student std = stdRepo.findByID(stdDTO.getId());

        stdRepo.remove(std);
    }

    //학생 id로 조회기능
    public StudentDTO retrieveByID(long id) {
        return ModelMapper.studentToDTO(stdRepo.findByID(id));
    }

    //학생 조건부 조회 기능
    public StudentDTO[] retrieveByOption(StudentOption... options) {
        return stdListToDTOArr(stdRepo.findByOption(options));
    }

    //학생 전체조회 기능
    public StudentDTO[] retrieveAll() {
        return stdListToDTOArr(stdRepo.findAll());
    }

    //해당 개설교과목에 수강중인 학생배열 조회
    public StudentDTO[] retrieveRegisteringStd(LectureDTO lectureDTO) {
        List<Registering> regs = regRepo.findByOption(new LectureIDOption(lectureDTO.getId()));
        StudentDTO[] res = new StudentDTO[regs.size()];

        int i = 0;
        for (Registering reg : regs) {
            res[i++] = ModelMapper.studentToDTO(
                    stdRepo.findByOption(new StudentCodeOption(reg.getStudentCode())).get(0)
            );
        }

        return res;
    }

    private StudentDTO[] stdListToDTOArr(List<Student> stdList) {
        StudentDTO[] dtos = new StudentDTO[stdList.size()];

        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = ModelMapper.studentToDTO(stdList.get(i));
        }

        return dtos;
    }
}
