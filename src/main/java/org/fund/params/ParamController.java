package org.fund.params;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidPersianDate;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class ParamController {
    private final ParamService service;
    private final JpaRepository repository;

    public ParamController(ParamService service, JpaRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(path = "/basicData/param/add")
    public void insert(@RequestBody ParamDto param) throws Exception {
        service.insert(param, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(path = "/basicData/param/edit")
    public void edit(@ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @NotEmpty(fieldName = "code") String code
            , @NotEmpty(fieldName = "value") String value) throws Exception {
        service.setValue(getFund(fundId), code, value, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = "/basicData/param/remove")
    public void remove(@NotEmpty(fieldName = "paramId")
                       @ValidateField(fieldName = "paramId", entityClass = Params.class) Long paramId) throws Exception {
        service.delete(paramId, RequestContext.getUserId(), RequestContext.getUuid());
    }


    @GetMapping(path = "/basicData/param/getStringValue")
    public String getStringValue(@NotEmpty(fieldName = "code") String code
            , @ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @ValidPersianDate(fieldName = "effectiveDate") String effectiveDate) {
        Optional<Long> optionalFundId = Optional.ofNullable(fundId);
        Optional<String> optionalDate = Optional.ofNullable(effectiveDate);

        return optionalFundId
                .map(this::getFund)
                .map(fund -> optionalDate.map(date -> service.getStringValue(fund, code, date))
                        .orElseGet(() -> service.getStringValue(fund, code)))
                .orElseGet(() -> optionalDate.map(date -> service.getStringValue(code, date))
                        .orElseGet(() -> service.getStringValue(code)));
    }

    @GetMapping(path = "/basicData/param/getLongValue")
    public Long getLongValue(@NotEmpty(fieldName = "code") String code
            , @ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @ValidPersianDate(fieldName = "effectiveDate") String effectiveDate) {
        Optional<Long> optionalFundId = Optional.ofNullable(fundId);
        Optional<String> optionalDate = Optional.ofNullable(effectiveDate);

        return optionalFundId
                .map(this::getFund)
                .map(fund -> optionalDate.map(date -> service.getLongValue(fund, code, date))
                        .orElseGet(() -> service.getLongValue(fund, code)))
                .orElseGet(() -> optionalDate.map(date -> service.getLongValue(code, date))
                        .orElseGet(() -> service.getLongValue(code)));
    }

    @GetMapping(path = "/basicData/param/getDoubleValue")
    public Double getDoubleValue(@NotEmpty(fieldName = "code") String code
            , @ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @ValidPersianDate(fieldName = "effectiveDate") String effectiveDate) {
        Optional<Long> optionalFundId = Optional.ofNullable(fundId);
        Optional<String> optionalDate = Optional.ofNullable(effectiveDate);

        return optionalFundId
                .map(this::getFund)
                .map(fund -> optionalDate.map(date -> service.getDoubleValue(fund, code, date))
                        .orElseGet(() -> service.getDoubleValue(fund, code)))
                .orElseGet(() -> optionalDate.map(date -> service.getDoubleValue(code, date))
                        .orElseGet(() -> service.getDoubleValue(code)));
    }

    @GetMapping(path = "/basicData/param/getFloatValue")
    public Float getFloatValue(@NotEmpty(fieldName = "code") String code
            , @ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @ValidPersianDate(fieldName = "effectiveDate") String effectiveDate) {
        Optional<Long> optionalFundId = Optional.ofNullable(fundId);
        Optional<String> optionalDate = Optional.ofNullable(effectiveDate);

        return optionalFundId
                .map(this::getFund)
                .map(fund -> optionalDate.map(date -> service.getFloatValue(fund, code, date))
                        .orElseGet(() -> service.getFloatValue(fund, code)))
                .orElseGet(() -> optionalDate.map(date -> service.getFloatValue(code, date))
                        .orElseGet(() -> service.getFloatValue(code)));
    }

    @GetMapping(path = "/basicData/param/getBooleanValue")
    public Boolean getBooleanValue(@NotEmpty(fieldName = "code") String code
            , @ValidateField(fieldName = "fundId", entityClass = Fund.class) Long fundId
            , @ValidPersianDate(fieldName = "effectiveDate") String effectiveDate) {
        Optional<Long> optionalFundId = Optional.ofNullable(fundId);
        Optional<String> optionalDate = Optional.ofNullable(effectiveDate);

        return optionalFundId
                .map(this::getFund)
                .map(fund -> optionalDate.map(date -> service.getBooleanValue(fund, code, date))
                        .orElseGet(() -> service.getBooleanValue(fund, code)))
                .orElseGet(() -> optionalDate.map(date -> service.getBooleanValue(code, date))
                        .orElseGet(() -> service.getBooleanValue(code)));
    }


    private Fund getFund(Long fundId) {
        return repository.findOne(Fund.class, fundId);
    }
}
