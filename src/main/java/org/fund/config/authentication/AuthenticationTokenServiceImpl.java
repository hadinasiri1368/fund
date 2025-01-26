package org.fund.config.authentication;

import org.fund.common.JwtUtil;
import org.fund.config.dataBase.TenantContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTokenServiceImpl implements TokenService<String, String> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String key = "_login_token";

    public AuthenticationTokenServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(String id, Users user) throws Exception {
        if (redisTemplate.hasKey(id))
            return redisTemplate.opsForValue().get(id).toString();
        String token = JwtUtil.createToken(user);
        redisTemplate.opsForValue().set(id, token);
        redisTemplate.opsForHash().put(TenantContext.getCurrentTenant() + key, token, id);
        return token;
    }

    @Override
    public boolean exists(String value) {
        return redisTemplate.opsForHash().hasKey(TenantContext.getCurrentTenant() + key, value);
    }

    @Override
    public void removeTokenById(String id) {
        if (!redisTemplate.hasKey(id))
            throw new FundException(AuthenticationExceptionType.USER_HAS_NOT_TOKEN);
        String token = redisTemplate.opsForValue().get(id).toString();
        redisTemplate.delete(id);
        redisTemplate.opsForHash().delete(TenantContext.getCurrentTenant() + key, token);
    }

    @Override
    public Users getTokenData(String id, String value) throws FundException {
        if (!exists(value) || !JwtUtil.validateToken(value, id))
            throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
        return JwtUtil.getTokenData(value);
    }
}
