package org.fund.baseInformation.customer.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Customer;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerBankAccountResponseDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "customerId")
    @ValidateField(fieldName = "customerId", entityClass = Customer.class)
    private Long customerId;
    private List<Long> bankAccountIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, customerId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> entities = new ArrayList<>();
        for (Long id : bankAccountIds) {
            entities.add(repository.findOne(entityClass, id));
        }
        return entities;
    }
}
