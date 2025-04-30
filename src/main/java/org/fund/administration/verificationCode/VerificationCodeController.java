package org.fund.administration.verificationCode;

import org.fund.administration.branch.FundBranchDto;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Fund;
import org.fund.model.FundBranch;
import org.fund.model.VerificationCode;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class VerificationCodeController {
    private final VerificationCodeService service;

    public VerificationCodeController(VerificationCodeService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/verificationCode/add")
    public void insert(@RequestBody VerificationCodeDto verificationCodeDto) throws Exception {
        service.insert(verificationCodeDto.toVerificationCode(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/verificationCode/edit")
    public void edit(@RequestBody VerificationCodeDto verificationCodeDto) throws Exception {
        service.update(verificationCodeDto.toVerificationCode(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/verificationCode/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = VerificationCode.class) Long verificationCodeId) throws Exception {
        service.delete(verificationCodeId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/verificationCode/{id}")
    public VerificationCode getVerificationCodeList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = VerificationCode.class) Long verificationCodeId) {
        List<VerificationCode> verificationCodes = service.list(verificationCodeId);
        return !FundUtils.isNull(verificationCodes) ? verificationCodes.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/verificationCode")
    public List<VerificationCode> getAllVerificationCodeList() {
        return service.list(null);
    }
}
