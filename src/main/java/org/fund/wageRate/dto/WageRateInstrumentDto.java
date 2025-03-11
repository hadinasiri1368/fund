package org.fund.wageRate.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WageRateInstrumentDto {
    private Long instrumentId;
    private Double bourseCoPurchase ;
    private Double bourseCoSale ;
    private Double depositCoPurchase ;
    private Double depositCoSale ;
    private Double bourseOrgPurchase ;
    private Double bourseOrgSale ;
    private Double itManagementPurchase ;
    private Double itManagementSale ;
    private Double interestPurchase ;
    private Double interestSale ;
    private Double taxPurchase ;
    private Double taxSale ;
    private Double rayanBoursePurchase ;
    private Double rayanBourseSale ;
}
