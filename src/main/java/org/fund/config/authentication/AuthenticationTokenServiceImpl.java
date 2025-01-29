package org.fund.config.authentication;

import org.fund.common.FundUtils;
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
    public String generateToken(Users user) throws Exception {
        if (redisTemplate.hasKey(getId(TenantContext.getCurrentTenant() + user.getId())))
            return redisTemplate.opsForValue().get(getId(TenantContext.getCurrentTenant() + user.getId())).toString();
        String token = JwtUtil.createToken(user);
        redisTemplate.opsForValue().set(getId(TenantContext.getCurrentTenant() + user.getId()), token);
        redisTemplate.opsForHash().put(TenantContext.getCurrentTenant() + key, token, user.getId());
        return token;
    }

    @Override
    public boolean exists(String value) {
        return redisTemplate.opsForHash().hasKey(TenantContext.getCurrentTenant() + key, value);
    }

    @Override
    public void removeTokenById(String id) {
        if (!redisTemplate.hasKey(getId(id)))
            throw new FundException(AuthenticationExceptionType.USER_HAS_NOT_TOKEN);
        String token = redisTemplate.opsForValue().get(getId(id)).toString();
        redisTemplate.delete(getId(id));
        redisTemplate.opsForHash().delete(TenantContext.getCurrentTenant() + key, token);
    }

    @Override
    public Users getTokenData(String id, String value) throws FundException {
        if (!exists(value) || !JwtUtil.validateToken(value, id))
            throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
        return JwtUtil.getTokenData(value);
    }

    private String getId(String id) {
        return id + key;
    }
}
