package org.fund.administration.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Calendar;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidPersianDate;

import java.util.List;

@Getter
@Setter
@Builder
public class CalendarDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "calendarDate")
    @ValidPersianDate(fieldName = "calendarDate")
    private String calendarDate;
    @NotEmpty(fieldName = "isOff")
    private Boolean isOff;
    @NotEmpty(fieldName = "isVacation")
    private Boolean isVacation;

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
