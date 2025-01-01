package org.fund.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.fund.dto.ExceptionDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(value = FundException.class)
    public ResponseEntity<ExceptionDto> handleFundException(FundException e, HttpServletRequest request) {
        String uuid = request.getHeader(Consts.HEADER_UUID_PARAM_NAME);
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
        log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                e.getStatus(), e.getMessage(), currentTime, uuid);
        return new ResponseEntity<>(ExceptionDto.builder()
                .code(e.getMessage())
                .message(FundUtils.getMessage(e.getMessage(), e.getParams()))
                .uuid(uuid)
                .time(currentTime)
                .build(), e.getStatus());
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ExceptionDto> handleDatabaseException(DataAccessException e, HttpServletRequest request) {
        String uuid = request.getHeader(Consts.HEADER_UUID_PARAM_NAME);
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));

        log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), currentTime, uuid);

        return new ResponseEntity<>(ExceptionDto.builder()
                .code(e.getMessage())
                .message(FundUtils.getMessage("database_exception.error"))
                .uuid(uuid)
                .time(currentTime)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionDto> handleGenralException(Exception e, HttpServletRequest request) {
        String uuid = request.getHeader(Consts.HEADER_UUID_PARAM_NAME);
        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));

        log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), currentTime, uuid);

        return new ResponseEntity<>(ExceptionDto.builder()
                .code(e.getMessage())
                .message(FundUtils.getMessage("unhandled_exception.error"))
                .uuid(uuid)
                .time(currentTime)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
