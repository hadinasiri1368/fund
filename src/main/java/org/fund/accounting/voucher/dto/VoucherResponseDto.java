package org.fund.accounting.voucher.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResponseDto {
    private Long id;
    private Long voucherTypeId;
    private String voucherTypeName;
    private Long fundBranchId;
    private String fundBranchName;
    private Long voucherStatusId;
    private String voucherStatusName;
    private Long fundId;
    private String fundName;
    private String code;
    private String voucherDate;
    private String comments;
    private Boolean isManual;
    private List<VoucherDetailResponseDto> voucherDetails;
}
