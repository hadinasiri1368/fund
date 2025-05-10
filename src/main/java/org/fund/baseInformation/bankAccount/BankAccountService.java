package org.fund.baseInformation.bankAccount;

import org.fund.common.FundUtils;
import org.fund.model.BankAccount;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.TradableItemDetailLedger;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private final JpaRepository repository;

    public BankAccountService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(BankAccount bankAccount, Long userId, String uuid) throws Exception {
        repository.save(bankAccount, userId, uuid);
    }

    public void update(BankAccount bankAccount, Long userId, String uuid) throws Exception {
        repository.save(bankAccount, userId, uuid);
    }

    public void delete(Long bankAccountId, Long userId, String uuid) throws Exception {
        repository.removeById(BankAccount.class, bankAccountId, userId, uuid);
    }

    public List<BankAccount> List(Long id) throws Exception {
        if (FundUtils.isNull(id))
            return repository.findAll(BankAccount.class).stream()
                    .filter(a -> a.getId().equals(id)).toList();
        return repository.findAll(BankAccount.class).stream().toList();
    }
}
