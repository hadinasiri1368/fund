package org.fund.baseInformation.financialInstitutionBankAccount;

import org.fund.common.FundUtils;
import org.fund.model.DetailLedger;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialInstitutionBankAccountService {
    private final JpaRepository repository;
    public FinancialInstitutionBankAccountService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(FinancialInstitutionBankAccount financialInstitutionBankAccount, Long userId, String uuid) throws Exception {
        repository.save(financialInstitutionBankAccount, userId, uuid);
    }

    public void delete(Long financialInstitutionBankAccountId, Long userId, String uuid) throws Exception {
        repository.removeById(FinancialInstitutionBankAccount.class, financialInstitutionBankAccountId, userId, uuid);
    }

    public void update(FinancialInstitutionBankAccount financialInstitutionBankAccount, Long userId, String uuid) throws Exception {
        repository.update(financialInstitutionBankAccount, userId, uuid);
    }

    public List<FinancialInstitutionBankAccount> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(FinancialInstitutionBankAccount.class);
        return repository.findAll(FinancialInstitutionBankAccount.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }
}
