package org.fund.administration.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.Calendar;
import org.fund.model.VerificationCode;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidPersianDate;

@Getter
@Setter
@Builder
public class CalendarDto {
    private Long id;
    @NotEmpty(fieldName = "calendarDate")
    @ValidPersianDate(fieldName = "calendarDate")
    private String calendarDate;
    @NotEmpty(fieldName = "isOff")
    private Boolean isOff;
    @NotEmpty(fieldName = "isVacation")
    private Boolean isVacation;

    public Calendar toCalendar() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Calendar.class);
    }
}
