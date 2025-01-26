package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthenticationExceptionType {
    TOKEN_IS_NULL(HttpStatus.UNAUTHORIZED, "authorization.token_is_null"),
    USER_HAS_TOKEN(HttpStatus.CONFLICT, "authorization.user_has_token"),
    USER_HAS_NOT_TOKEN(HttpStatus.NOT_FOUND, "authorization.user_has_not_token"),
    USER_IS_NOT_ACTIVE(HttpStatus.FORBIDDEN, "authorization.user_is_not_active"),
    USERNAME_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "authorization.username_password_invalid")
    ;
    private final HttpStatus httpStatus;
    private final String messageKey;

    AuthenticationExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
