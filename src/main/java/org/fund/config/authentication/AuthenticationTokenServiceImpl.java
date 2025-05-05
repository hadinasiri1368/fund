package org.fund.config.authentication;

import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.config.dataBase.TenantContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationTokenServiceImpl implements TokenService<String, String> {
    @Value("${jwt.expirationMinutes}")
    private long expirationMinutes;

    private final RedisTemplate<String, Object> redisTemplate;
    private final String key = "_login_token";

    public AuthenticationTokenServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(Users user) throws Exception {
        if (redisTemplate.hasKey(getId(TenantContext.getCurrentTenant() + user.getId())))
            return redisTemplate.opsForValue().get(getId(TenantContext.getCurrentTenant() + user.getId())).toString();
        String token = JwtUtil.createToken(user);
        redisTemplate.opsForValue().set(getId(TenantContext.getCurrentTenant() + user.getId()), token, expirationMinutes, TimeUnit.MINUTES);
        redisTemplate.opsForHash().put(TenantContext.getCurrentTenant() + key, token, user.getId());
        redisTemplate.expire(TenantContext.getCurrentTenant() + key, expirationMinutes, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public boolean exists(String value) {
        return redisTemplate.opsForHash().hasKey(TenantContext.getCurrentTenant() + key, value);
    }

    @Override
    public void removeTokenById(String tenantId, Long userId, String token) {
        if (!redisTemplate.hasKey(getId(tenantId + userId)))
            throw new FundException(AuthenticationExceptionType.USER_HAS_NOT_TOKEN);
        String oldToken = redisTemplate.opsForValue().get(getId(tenantId + userId)).toString();
        if(!oldToken.equals(token))
            throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
        redisTemplate.delete(getId(tenantId + userId));
        redisTemplate.opsForHash().delete(tenantId + key, oldToken);
    }

    @Override
    public Users getTokenData(String id, String value) throws FundException {
        if (!exists(value) || !JwtUtil.validateToken(value))
            throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
        return JwtUtil.getTokenData(value);
    }

    private String getId(String id) {
        return id + key;
    }
}
