package org.fund.administration.wageRate.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.fund.dto.DtoConvertible;
import org.fund.model.WageRate;
import org.fund.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WageRateInstrumentDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, targetType);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
