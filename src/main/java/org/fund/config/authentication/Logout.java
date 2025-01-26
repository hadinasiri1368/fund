package org.fund.config.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class Logout implements LogoutHandler {
    private final TokenService tokenService;

    public Logout(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        tokenService.removeTokenById(RequestContext.getTokenId());
    }
}
