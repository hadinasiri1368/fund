package org.fund.config.authentication;

import org.fund.exception.FundException;
import org.fund.model.Users;

public interface TokenService<K, V> {
    String generateToken(Users user) throws Exception;

    boolean exists(V value);

    void removeTokenById(String tenantId, Long userId, String token);

    Users getTokenData(K id, V value) throws FundException;
}
