package org.fund.baseInformation.bankAccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.repository.JpaRepository;

import java.util.List;

@Getter
@Setter
public class BankAccountTypeDto implements DtoConvertible {
    private Long id;
    private String name;

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
