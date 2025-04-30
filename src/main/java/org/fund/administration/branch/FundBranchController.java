package org.fund.administration.branch;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Fund;
import org.fund.model.FundBranch;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class FundBranchController {
    private final FundBranchService service;

    public FundBranchController(FundBranchService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/branch/add")
    public void insert(@RequestBody FundBranchDto fundBranchDto) throws Exception {
        service.insert(fundBranchDto.toFundBranch(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/branch/edit")
    public void edit(@RequestBody FundBranchDto fundBranchDto) throws Exception {
        service.update(fundBranchDto.toFundBranch(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/branch/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = FundBranch.class) Long branchId) throws Exception {
        service.delete(branchId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/branch/{id}")
    public FundBranch getFundList(@PathVariable(value = "id") @ValidateField(fieldName = "id", entityClass = FundBranch.class) Long branchId) {
        List<FundBranch> fundBranches = service.list(branchId);
        return !FundUtils.isNull(fundBranches) ? fundBranches.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/branch")
    public List<FundBranch> getAllFundList() {
        return service.list(null);
    }
}
