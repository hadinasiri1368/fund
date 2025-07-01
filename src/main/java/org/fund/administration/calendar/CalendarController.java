package org.fund.administration.calendar;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
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
    private final GenericDtoMapper mapper;

    public CalendarController(CalendarService service, GenericDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/add")
    public void insert(@RequestBody CalendarDto calendarDto) throws Exception {
        service.insert(mapper.toEntity(Calendar.class, calendarDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/edit")
    public void edit(@RequestBody CalendarDto calendarDto) throws Exception {
        service.update(mapper.toEntity(Calendar.class, calendarDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Calendar.class) Long calendarId) throws Exception {
        service.delete(calendarId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar/{id}")
    public Calendar getCalendarList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Calendar.class) Long calendarId) {
        List<Calendar> calendars = service.list(calendarId);
        return !FundUtils.isNull(calendars) ? calendars.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/administration/calendar")
    public List<Calendar> getAllCalendarList() {
        return service.list(null);
    }
}
