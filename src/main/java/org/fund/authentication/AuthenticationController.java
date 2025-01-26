package org.fund.authentication;

import org.fund.config.authentication.TokenService;
import org.fund.config.request.RequestContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping(path = "/login")
    public String login(String username, String password) throws Exception {
        return service.login(username, password);
    }
}
