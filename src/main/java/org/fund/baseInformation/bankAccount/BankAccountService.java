package org.fund.baseInformation.bankAccount;

import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.model.view.external.Bank;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        repository.update(bankAccount, userId, uuid);
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

    public List<BankDto> getBankList(Long id) {
        List<Bank> banks = FundUtils.isNull(id)
                ? repository.findAll(Bank.class)
                : repository.findBy(Bank.class, "id", id);

        return banks.stream()
                .map(Bank::toDto)
                .collect(Collectors.toList());
    }

    public List<BankAccountTypeDto> getBankAccountTypeList(Long id) {
        List<BankAccountType> bankAccountTypes = FundUtils.isNull(id)
                ? repository.findAll(BankAccountType.class)
                : repository.findBy(BankAccountType.class, "id", id);

        return bankAccountTypes.stream()
                .map(BankAccountType::toDto)
                .collect(Collectors.toList());
    }

}
