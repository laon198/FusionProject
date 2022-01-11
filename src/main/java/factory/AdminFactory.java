package factory;

import domain.model.Admin;
import dto.AdminDTO;

public class AdminFactory {
    public  Admin create(AdminDTO dto){
        return Admin.builder(
                dto.getName(), dto.getDepartment(),
                dto.getBirthDate(), dto.getAdminCode())
                .build();
    }
}