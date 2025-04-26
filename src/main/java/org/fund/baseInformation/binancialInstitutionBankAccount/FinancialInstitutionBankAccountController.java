package org.fund.baseInformation.binancialInstitutionBankAccount;

import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.DetailLedger;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class FinancialInstitutionBankAccountController {
    private final FinancialInstitutionBankAccountService service;
    public FinancialInstitutionBankAccountController(FinancialInstitutionBankAccountService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/add")
    public void insert(@RequestBody FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto) throws Exception {
        service.insert(financialInstitutionBankAccountDto.toFinancialInstitutionBankAccount(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/edit")
    public void edit(@RequestBody FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto) throws Exception {
        service.update(financialInstitutionBankAccountDto.toFinancialInstitutionBankAccount(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = FinancialInstitutionBankAccount.class) Long financialInstitutionBankAccountId) throws Exception {
        service.delete(financialInstitutionBankAccountId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/{id}")
    public List<FinancialInstitutionBankAccount> getFinancialInstitutionBankAccount(@PathVariable @ValidateField(fieldName = "id", entityClass = FinancialInstitutionBankAccount.class) Long financialInstitutionBankAccountId) {
        return service.list(financialInstitutionBankAccountId);
    }
}
