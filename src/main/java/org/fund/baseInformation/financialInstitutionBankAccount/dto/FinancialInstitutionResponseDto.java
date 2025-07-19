package org.fund.baseInformation.financialInstitutionBankAccount.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedgerType;
import org.fund.model.FinancialInstitution;
import org.fund.model.Person;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class FinancialInstitutionResponseDto implements DtoConvertible {
    private Long id;
    private String name;
    private DetailLedgerDto detailLedgerDto;
    private PersonDto person;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return null;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
