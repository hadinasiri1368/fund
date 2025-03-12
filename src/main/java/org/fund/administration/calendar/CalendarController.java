package org.fund.administration.calendar;

import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Calendar;
import org.fund.model.VerificationCode;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class CalendarController {
    private final CalendarService service;
    public CalendarController(CalendarService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/add")
    public void insert(@RequestBody CalendarDto calendarDto) throws Exception {
        service.insert(calendarDto.toCalendar(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/edit")
    public void edit(@RequestBody CalendarDto calendarDto) throws Exception {
        service.update(calendarDto.toCalendar(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Calendar.class) Long calendarId) throws Exception {
        service.delete(calendarId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/{id}")
    public List<Calendar> getFundList(@PathVariable @ValidateField(fieldName = "id", entityClass = Calendar.class) Long calendarId) {
        return service.list(calendarId);
    }
}
