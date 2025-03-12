package org.fund.administration.calendar;

import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.model.Calendar;
import org.fund.model.Fund;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CalendarService {
    private final JpaRepository repository;

    public CalendarService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(Calendar calendar, Long userId, String uuid) throws Exception {
        repository.save(calendar, userId, uuid);
    }

    public void delete(Long calendarId, Long userId, String uuid) throws Exception {
        repository.removeById(Calendar.class, calendarId, userId, uuid);
    }

    public void update(Calendar calendar, Long userId, String uuid) throws Exception {
        repository.update(calendar, userId, uuid);
    }

    public List<Calendar> list(Long id) {
        if (FundUtils.isNull(id)) return repository.findAll(Calendar.class);
        return repository.findAll(Calendar.class).stream().filter(a -> a.getId().equals(id)).toList();
    }

    public String getTomorrowDate(String date) {
        Calendar calendar = repository.findAll(Calendar.class).stream()
                .filter(a -> a.getCalendarDate() != null && date != null && a.getCalendarDate().compareTo(date) > 0)
                .sorted(Comparator.comparing(Calendar::getCalendarDate))
                .findFirst()
                .orElse(null);
        if (FundUtils.isNull(calendar))
            return null;
        return calendar.getCalendarDate();
    }

    public String getYesterdayDate(String date) {
        Calendar calendar = repository.findAll(Calendar.class).stream()
                .filter(a -> a.getCalendarDate() != null && date != null && a.getCalendarDate().compareTo(date) < 0)
                .sorted(Comparator.comparing(Calendar::getCalendarDate).reversed())
                .findFirst()
                .orElse(null);
        if (FundUtils.isNull(calendar))
            return null;
        return calendar.getCalendarDate();
    }

    public String getNextWorkingDate(String date) {
        Calendar calendar = repository.findAll(Calendar.class).stream()
                .filter(a -> a.getCalendarDate() != null && date != null &&
                        !a.getIsOff() && !a.getIsVacation() &&
                        a.getCalendarDate().compareTo(date) > 0)
                .sorted(Comparator.comparing(Calendar::getCalendarDate))
                .findFirst()
                .orElse(null);
        if (FundUtils.isNull(calendar))
            return null;
        return calendar.getCalendarDate();
    }

    public String getPreviousWorkingDate(String date) {
        Calendar calendar = repository.findAll(Calendar.class).stream()
                .filter(a -> a.getCalendarDate() != null && date != null &&
                        !a.getIsOff() && !a.getIsVacation() &&
                        a.getCalendarDate().compareTo(date) < 0)
                .sorted(Comparator.comparing(Calendar::getCalendarDate).reversed())
                .findFirst()
                .orElse(null);
        if (FundUtils.isNull(calendar))
            return null;
        return calendar.getCalendarDate();
    }
}
