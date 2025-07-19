package org.fund.baseInformation.financialInstitutionBankAccount.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
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
public class FinancialInstitutionRequestDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    @NotEmpty(fieldName = "detailLedgerTypeId")
    @ValidateField(fieldName = "detailLedgerTypeId", entityClass = DetailLedgerType.class)
    private Long detailLedgerTypeId;
    private PersonDto person;
    List<FinancialInstitutionBankAccountDto> bankAccounts;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof FinancialInstitution financialInstitution) {
            if (!FundUtils.isNull(person) && !FundUtils.isNull(person.getId()))
                financialInstitution.setPerson(getPerson(repository, person.getId()));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private Person getPerson(JpaRepository repository, Long personId) {
        return repository.findOne(Person.class, personId);
    }
}
