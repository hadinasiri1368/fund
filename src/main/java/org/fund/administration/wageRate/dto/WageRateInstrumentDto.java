package org.fund.administration.wageRate.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WageRateInstrumentDto {
    private Long id;
    private Boolean isContract;
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
