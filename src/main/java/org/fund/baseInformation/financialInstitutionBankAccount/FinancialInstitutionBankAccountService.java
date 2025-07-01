package org.fund.baseInformation.financialInstitutionBankAccount;

import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.accounting.detailLedger.constant.DetailLedgerType;
import org.fund.baseInformation.bankAccount.BankAccountService;
import org.fund.common.FundUtils;
import org.fund.dto.GenericDtoMapper;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialInstitutionBankAccountService {
    private final JpaRepository repository;
    private final BankAccountService bankAccountService;
    private final DetailLedgerService detailLedgerService;
    private final GenericDtoMapper mapper;

    public FinancialInstitutionBankAccountService(JpaRepository repository
            , BankAccountService bankAccountService
            , GenericDtoMapper mapper
            , DetailLedgerService detailLedgerService) {
        this.repository = repository;
        this.bankAccountService = bankAccountService;
        this.detailLedgerService = detailLedgerService;
        this.mapper = mapper;
    }

    @Transactional
    public void insert(FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto, Fund fund, Long userId, String uuid) throws Exception {
        DetailLedgerType detailLedgerType = DetailLedgerType.getItemById(financialInstitutionBankAccountDto.getDetailLedgerTypeId());
        if (FundUtils.isNull(detailLedgerType))
            throw new FundException(GeneralExceptionType.FIELD_NOT_VALID, new Object[]{"detailLedgerTypeId"});
        DetailLedger detailLedger = detailLedgerService.get(financialInstitutionBankAccountDto.getName(), fund, detailLedgerType);
        detailLedgerService.insert(detailLedger, userId, uuid);
        BankAccount bankAccount = mapper.toEntity(BankAccount.class, financialInstitutionBankAccountDto.getBankAccount());
        bankAccountService.insert(bankAccount, userId, uuid);
        FinancialInstitutionBankAccount financialInstitutionBankAccount = mapper.toEntity(FinancialInstitutionBankAccount.class, financialInstitutionBankAccountDto);
        financialInstitutionBankAccount.setBankAccount(bankAccount);
        financialInstitutionBankAccount.setDetailLedger(detailLedger);
        repository.save(financialInstitutionBankAccount, userId, uuid);
    }

    public void delete(Long financialInstitutionBankAccountId, Long userId, String uuid) throws Exception {
        FinancialInstitutionBankAccount financialInstitutionBankAccount = repository.findOne(FinancialInstitutionBankAccount.class, financialInstitutionBankAccountId);
        detailLedgerService.delete(financialInstitutionBankAccount.getDetailLedger().getId(), userId, uuid);
        bankAccountService.delete(financialInstitutionBankAccount.getBankAccount().getId(), userId, uuid);
        repository.removeById(FinancialInstitutionBankAccount.class, financialInstitutionBankAccountId, userId, uuid);
    }


    public List<FinancialInstitutionBankAccount> list(Long id) {
        if (FundUtils.isNull(id))
            return repository.findAll(FinancialInstitutionBankAccount.class);
        return repository.findAll(FinancialInstitutionBankAccount.class).stream()
                .filter(a -> a.getId().equals(id)).toList();
    }
}
