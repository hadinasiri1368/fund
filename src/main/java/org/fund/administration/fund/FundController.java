package org.fund.administration.fund;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
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
    private final FundService service;
    private final GenericDtoMapper mapper;

    public FundController(FundService service, GenericDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/fund/add")
    public void insert(@RequestBody FundDto fundDto) throws Exception {
        service.insert(mapper.toEntity(Fund.class, fundDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/fund/edit")
    public void edit(@RequestBody FundDto fundDto) throws Exception {
        service.update(mapper.toEntity(Fund.class, fundDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/fund/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Fund.class) Long fundId) throws Exception {
        service.delete(fundId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/fund/{id}")
    public Fund getFundList(@PathVariable @ValidateField(fieldName = "id", entityClass = Fund.class) Long fundId) {
        List<Fund> funds = service.list(getFund(fundId));
        return !FundUtils.isNull(funds) ? funds.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/fund")
    public List<Fund> getAllFundList() {
        return service.list(getFund(null));
    }

    private Fund getFund(Long fundId) {
        if (FundUtils.isNull(fundId)) return null;
        return service.getFund(fundId);
    }
}
