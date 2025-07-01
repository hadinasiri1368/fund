package org.fund.administration.verificationCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.FundBranch;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.util.List;

@Getter
@Setter
@Builder
public class VerificationCodeDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "seed")
    private String seed;
    @NotEmpty(fieldName = "counter")
    private Long counter;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;

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
