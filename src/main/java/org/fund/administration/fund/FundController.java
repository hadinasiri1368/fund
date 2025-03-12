package org.fund.administration.fund;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Fund;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class FundController {
    private final JpaRepository repository;
    private final FundService service;

    public FundController(FundService service, JpaRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/basicData/fund/add")
    public void insert(@RequestBody FundDto fundDto) throws Exception {
        service.insert(fundDto.toFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/basicData/fund/edit")
    public void edit(@RequestBody FundDto fundDto) throws Exception {
        service.update(fundDto.toFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/basicData/fund/remove")
    public void remove(@NotEmpty(fieldName = "fundId") Long fundId) throws Exception {
        service.delete(fundId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/basicData/fund")
    public List<Fund> getFundList(@ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId) {
        return service.list(getFund(fundId));
    }

    private Fund getFund(Long fundId) {
        if (FundUtils.isNull(fundId)) return null;
        return repository.findOne(Fund.class, fundId);
    }
}
