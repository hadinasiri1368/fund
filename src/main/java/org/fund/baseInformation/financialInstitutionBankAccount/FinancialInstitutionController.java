package org.fund.baseInformation.financialInstitutionBankAccount;

import org.fund.administration.fund.FundService;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionBankAccountDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionRequestDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionResponseDto;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.FinancialInstitution;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class FinancialInstitutionController {
    private final FinancialInstitutionService service;
    private final FundService fundService;

    public FinancialInstitutionController(FinancialInstitutionService service, FundService fundService) {
        this.service = service;
        this.fundService = fundService;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitution/add")
    public void insert(@RequestBody FinancialInstitutionRequestDto financialInstitutionBankAccountDto) throws Exception {
        service.insert(financialInstitutionBankAccountDto, fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitution/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = FinancialInstitution.class) Long financialInstitutionId) throws Exception {
        service.delete(financialInstitutionId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitution/{id}")
    public FinancialInstitutionResponseDto getFinancialInstitutionBankAccount(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = FinancialInstitutionBankAccount.class) Long financialInstitutionId) {
        List<FinancialInstitutionResponseDto> financialInstitutionBankAccounts = service.list(financialInstitutionId);
        return !FundUtils.isNull(financialInstitutionBankAccounts) ? financialInstitutionBankAccounts.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/financialInstitution")
    public List<FinancialInstitutionResponseDto> getAllFinancialInstitutionBankAccount() {
        return service.list(null);
    }
}
