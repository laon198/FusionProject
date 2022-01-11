package application;

import domain.model.Account;
import domain.repository.AccountRepository;
import infra.database.option.account.AccountIDOption;
import dto.AccountDTO;
import dto.ModelMapper;

//계정과 관련된 기능을 수행하는 객체
public class AccountAppService {
    private AccountRepository accRepo;

    public AccountAppService(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    //로그인 기능 수행
    public AccountDTO login(AccountDTO accDTO) throws IllegalArgumentException{
        //데이터베이스에서 아이디로 계정정보 가져옴
        Account acc = accRepo.findByOption(new AccountIDOption(accDTO.getId())).get(0);

        //비밀번호 일치여부 확인
        if(!acc.checkPassword(accDTO.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //로그인 성공시, 해당 계정정보 반환(DTO로 반환)
        return ModelMapper.accountToDTO(acc);
    }

    //비밀번호 변경 수행
    public void changePassword(AccountDTO accDTO){
        //데이터베이스에서 아이디로 계정정보 가져옴
        Account acc = accRepo.findByOption(new AccountIDOption(accDTO.getId())).get(0);
        //비밀번호 변경
        acc.changePassword(accDTO.getPassword());
        //변경된 계정 데이터베이스에 저장
        accRepo.save(acc);
    }
}
