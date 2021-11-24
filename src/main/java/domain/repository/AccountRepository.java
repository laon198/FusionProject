package domain.repository;

import domain.model.Account;
import infra.database.option.account.AccountOption;

import java.util.List;

public interface AccountRepository {
    public Account findByID(long id);
    public List<Account> findByOption(AccountOption... options);
    public long save(Account account);
}
