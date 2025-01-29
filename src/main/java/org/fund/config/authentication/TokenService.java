package org.fund.config.authentication;

import org.fund.exception.FundException;
import org.fund.model.Users;

public interface TokenService<K, V> {
    String generateToken(Users user) throws Exception;
    boolean exists(V value);
    void removeTokenById(K id);
    Users getTokenData(K id, V value) throws FundException;
}
