package factory;

import domain.model.Account;
import dto.MemberDTO;

public class AccountFactory {
    public Account create(MemberDTO dto, long memberID, String position){
        return Account.builder(dto.getCode(), dto.getBirthDate(), position, memberID)
                .build();
    }
}
