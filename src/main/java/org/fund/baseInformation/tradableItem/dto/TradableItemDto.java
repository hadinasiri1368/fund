package org.fund.baseInformation.tradableItem.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.repository.JpaRepository;

import java.util.List;

@Getter
@Setter
public class TradableItemDto implements DtoConvertible {
    private Long id;
    private String description;
    private Boolean bourseAccount;
    private Long typeId;
    private Long typeName;
    private Integer tradableItemGroup;


    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);
        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
