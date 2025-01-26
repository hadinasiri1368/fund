package org.fund.config.request;

import org.fund.config.dataBase.TenantContext;
import org.fund.model.Users;

public class RequestContext {
    private static final ThreadLocal<String> uuid = new ThreadLocal<>();
    private static final ThreadLocal<Users> user = new ThreadLocal<>();
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setUuid(String uuidValue) {
        uuid.set(uuidValue);
    }

    public static void setUser(Users userValue) {
        user.set(userValue);
    }

    public static void setToken(String tokenValue) {
        token.set(tokenValue);
    }

    public static String getUuid() {
        return uuid.get();
    }

    public static Users getUser() {
        return user.get();
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    public static String getToken() {
        return token.get();
    }

    public static String getTokenId() {
        return TenantContext.getCurrentTenant() + "_" + getUserId();
    }

    public static void clear() {
        uuid.remove();
        user.remove();
        token.remove();
    }
}

