package domain.repository;

import domain.model.Account;

public interface AccountRepository {
    public Account findByID(long id);
    public long save(Account account);
}
