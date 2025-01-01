package org.fund.config.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.springframework.web.servlet.HandlerInterceptor;


public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = request.getAttribute(Consts.HEADER_UUID_PARAM_NAME).toString();
        String token = FundUtils.getToken(request);
        Long userId = FundUtils.getUserId(token);

        // Set values in ThreadLocal for easy access
        RequestContext.setUuid(uuid);
        RequestContext.setUserId(userId);
        RequestContext.setToken(token);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Clear values after the request is completed to avoid memory leaks
        RequestContext.clear();
    }
}

