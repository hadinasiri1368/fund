package org.fund.baseInformation.financialInstitutionBankAccount;

import org.fund.administration.fund.FundService;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
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
    private FundService fundService;

    public FinancialInstitutionBankAccountController(FinancialInstitutionBankAccountService service, FundService fundService) {
        this.service = service;
        this.fundService = fundService;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/add")
    public void insert(@RequestBody FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto) throws Exception {
        service.insert(financialInstitutionBankAccountDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = FinancialInstitutionBankAccount.class) Long financialInstitutionBankAccountId) throws Exception {
        service.delete(financialInstitutionBankAccountId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount/{id}")
    public FinancialInstitutionBankAccount getFinancialInstitutionBankAccount(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = FinancialInstitutionBankAccount.class) Long financialInstitutionBankAccountId) {
        List<FinancialInstitutionBankAccount> financialInstitutionBankAccounts = service.list(financialInstitutionBankAccountId);
        return !FundUtils.isNull(financialInstitutionBankAccounts) ? financialInstitutionBankAccounts.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitutionBankAccount")
    public List<FinancialInstitutionBankAccount> getAllFinancialInstitutionBankAccount() {
        return service.list(null);
    }
}
