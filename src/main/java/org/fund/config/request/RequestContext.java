package org.fund.config.request;

public class RequestContext {
    private static final ThreadLocal<String> uuid = new ThreadLocal<>();
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setUuid(String uuidValue) {
        uuid.set(uuidValue);
    }

    public static void setUserId(Long userIdValue) {
        userId.set(userIdValue);
    }

    public static void setToken(String tokenValue) {
        token.set(tokenValue);
    }

    public static String getUuid() {
        return uuid.get();
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static String getToken() {
        return token.get();
    }

    public static void clear() {
        uuid.remove();
        userId.remove();
        token.remove();
    }
}

