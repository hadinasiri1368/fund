package org.fund.authentication;

import org.fund.config.authentication.TokenService;
import org.fund.config.request.RequestContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final JpaRepository repository;
    private final TokenService tokenService;

    public AuthenticationService(JpaRepository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    public String login(String username, String password) throws Exception {
        Optional<Users> user = repository.findAll(Users.class).stream()
                .filter(a -> a.getUsername().equals(username)
                        && a.getPassword().equals(password)
                )
                .findFirst();
        if (!user.isPresent()) {
            throw new FundException(AuthenticationExceptionType.USERNAME_PASSWORD_INVALID);
        }
        if (!user.get().getIsActive()) {
            throw new FundException(AuthenticationExceptionType.USER_IS_NOT_ACTIVE);
        }
        return tokenService.generateToken(RequestContext.getTokenId(), user.get());
    }
}
