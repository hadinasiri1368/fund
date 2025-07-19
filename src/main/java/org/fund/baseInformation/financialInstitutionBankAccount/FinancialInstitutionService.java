package org.fund.baseInformation.financialInstitutionBankAccount;

import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.accounting.detailLedger.constant.DetailLedgerType;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.baseInformation.bankAccount.BankAccountService;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionBankAccountDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionRequestDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionResponseDto;
import org.fund.common.FundUtils;
import org.fund.dto.GenericDtoMapper;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinancialInstitutionService {
    private final JpaRepository repository;
    private final BankAccountService bankAccountService;
    private final DetailLedgerService detailLedgerService;
    private final GenericDtoMapper mapper;

    public FinancialInstitutionService(JpaRepository repository
            , BankAccountService bankAccountService
            , GenericDtoMapper mapper
            , DetailLedgerService detailLedgerService) {
        this.repository = repository;
        this.bankAccountService = bankAccountService;
        this.detailLedgerService = detailLedgerService;
        this.mapper = mapper;
    }

    @Transactional
    public void insert(FinancialInstitutionRequestDto financialInstitutionDto, Fund fund, Long userId, String uuid) throws Exception {
        DetailLedgerType detailLedgerType = DetailLedgerType.getItemById(financialInstitutionDto.getDetailLedgerTypeId());
        DetailLedger detailLedger = detailLedgerService.get(financialInstitutionDto.getName(), fund, detailLedgerType);
        detailLedgerService.insert(detailLedger, userId, uuid);

        FinancialInstitution financialInstitution = mapper.toEntity(FinancialInstitution.class, financialInstitutionDto);

        Person person = financialInstitution.getPerson();
        if (!FundUtils.isNull(person)) {
            repository.save(person, userId, uuid);
            financialInstitution.setPerson(person);
        }
        financialInstitution.setDetailLedger(detailLedger);


        repository.save(financialInstitution, userId, uuid);

        if (!FundUtils.isNull(financialInstitutionDto.getBankAccounts()) && financialInstitutionDto.getBankAccounts().size() > 0) {
            for (FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto : financialInstitutionDto.getBankAccounts()) {
                FinancialInstitutionBankAccountDto bankAccountDto = new FinancialInstitutionBankAccountDto();
                BankAccount bankAccount = new BankAccount();
                bankAccountDto = mapper.toEntity(FinancialInstitutionBankAccountDto.class, financialInstitutionBankAccountDto);
                bankAccount = mapper.toEntity(BankAccount.class, bankAccountDto);
                bankAccountService.insert(bankAccount, userId, uuid);

                FinancialInstitutionBankAccount financialInstitutionBankAccount = FinancialInstitutionBankAccount.builder()
                        .bankAccount(bankAccount)
                        .financialInstitution(financialInstitution)
                        .isActive(true)
                        .build();

                repository.save(financialInstitutionBankAccount, userId, uuid);
            }
        }


    }

    public void delete(Long financialInstitutionId, Long userId, String uuid) throws Exception {
        List<FinancialInstitutionBankAccount> list = repository.findAll(FinancialInstitutionBankAccount.class)
                .stream()
                .filter(a -> a.getFinancialInstitution().getId().equals(financialInstitutionId))
                .toList();
        if (!FundUtils.isNull(list) && list.size() > 0) {
            for (FinancialInstitutionBankAccount financialInstitutionBankAccount : list) {
                bankAccountService.delete(financialInstitutionBankAccount.getBankAccount().getId(), userId, uuid);
            }
            repository.batchRemove(list, userId, uuid);
        }

        FinancialInstitution financialInstitution = repository.findOne(FinancialInstitution.class, financialInstitutionId);
        detailLedgerService.delete(financialInstitution.getDetailLedger().getId(), userId, uuid);
        repository.removeById(FinancialInstitution.class, financialInstitutionId, userId, uuid);
    }


    public List<FinancialInstitutionResponseDto> list(Long id) {
        if (FundUtils.isNull(id)) {
            return repository.findAll(FinancialInstitution.class).stream().map(a -> a.toDto()).toList();
        }
        return List.of(repository.findOne(FinancialInstitution.class, id).toDto());
    }

    public List<FinancialInstitutionBankAccountDto> listBankAccoun(Long fiId) {
        return repository.findAll(FinancialInstitutionBankAccount.class).stream()
                .filter(a -> a.getFinancialInstitution().getId().equals(fiId))
                .map(a -> a.toDto())
                .toList();
    }
}
