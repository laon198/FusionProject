package application;

import domain.model.Account;
import domain.repository.AccountRepository;
import infra.database.option.account.AccountIDOption;
import infra.dto.AccountDTO;
import infra.dto.ModelMapper;

public class AccountAppService {
    private AccountRepository accRepo;

    public AccountAppService(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    public AccountDTO login(AccountDTO accDTO) throws IllegalArgumentException{
        Account acc = accRepo.findByOption(new AccountIDOption(accDTO.getId())).get(0);

        if(!acc.checkPassword(accDTO.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return ModelMapper.accountToDTO(acc);
    }

    public void changePassword(AccountDTO accDTO){
        //TODO : 해당아이디 없을때 예외처리필요
        Account acc = accRepo.findByOption(new AccountIDOption(accDTO.getId())).get(0);
        acc.changePassword(accDTO.getPassword());
        accRepo.save(acc);
    }
}
