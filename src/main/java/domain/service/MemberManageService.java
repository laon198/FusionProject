package domain.service;

import domain.model.*;

public class MemberManageService {

    public Student createStudent(String name, String birthDate, String department,
                                    int year, String studentCode){
        //TODO : 학번 중복체크?
        return Student.builder()
                .name(name)
                .birthDate(birthDate)
                .department(department)
                .year(year)
                .studentCode(studentCode)
                .build();
    }

    public Professor createProfessor(String name, String birthDate, String department,
                                        String professorCode, String telePhone){
        return Professor.builder()
                    .name(name)
                    .birthDate(birthDate)
                    .department(department)
                    .professorCode(professorCode)
                    .telePhone(telePhone)
                    .build();
    }

    public Admin createAdmin(String name, String birthDate, String department,
                                String adminCode) {
        return Admin.builder()
                .name(name)
                .birthDate(birthDate)
                .department(department)
                .adminCode(adminCode)
                .build();
    }

    public Account createAccount(String id, String password, long memberID){
        return Account.builder()
                .id(id)
                .password(password)
                .memberID(memberID)
                .build();
    }

}
