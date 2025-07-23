package org.fund.baseInformation.bankAccount;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.repository.JpaRepository;

import java.util.List;

@Getter
@Setter
public class BankDto  implements DtoConvertible {
    private Long id;
    private String name;
    private Boolean isValid;

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
