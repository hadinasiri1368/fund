package org.fund.accounting.voucher;

import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.accounting.voucher.dto.VoucherDetailDto;
import org.fund.accounting.voucher.dto.VoucherDto;
import org.fund.accounting.voucher.dto.VoucherResponseDto;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.model.DetailLedger;
import org.fund.model.Voucher;
import org.fund.model.VoucherDetail;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class VoucherController {
    private final VoucherService service;
    private final GenericDtoMapper mapper;

    public VoucherController(VoucherService service, GenericDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher/add")
    public void insert(@RequestBody VoucherDto voucherDto) throws Exception {
        service.insert(mapper.toEntity(Voucher.class, voucherDto), mapper.toEntityList(VoucherDetail.class,voucherDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher/edit")
    public void edit(@RequestBody VoucherDto voucherDto) throws Exception {
        service.update(mapper.toEntity(Voucher.class, voucherDto), mapper.toEntityList(VoucherDetail.class,voucherDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Voucher.class) Long voucherId) throws Exception {
        service.delete(voucherId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher/remove")
    public void remove(@RequestBody List<VoucherDetailDto> voucherDetailDto) throws Exception {
        List<VoucherDetail> list = new ArrayList<>();
        for (VoucherDetailDto dto : voucherDetailDto) {
            list.add(mapper.toEntity(VoucherDetail.class, dto));
        }
        service.delete(list, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher/{id}")
    public VoucherResponseDto getDetailLedgerList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Voucher.class) Long id) {
        return service.list(id).get(0);
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/voucher")
    public List<VoucherResponseDto> getDetailLedgerList() {
        return service.list(null);
    }
}
