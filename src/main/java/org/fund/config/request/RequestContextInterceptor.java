package org.fund.config.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.authentication.permission.PermissionService;
import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.constant.Consts;
import org.fund.model.Permission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;


public class RequestContextInterceptor implements HandlerInterceptor {
    private final PermissionService permissionService;

    public RequestContextInterceptor(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (permissionService.isBypassedUrl(request.getRequestURI()))
            return true;
        String uuid = request.getAttribute(Consts.HEADER_UUID_PARAM_NAME).toString();
        RequestContext.setUuid(uuid);
        if (permissionService.isSensitiveUrl(request.getRequestURI()))
            return true;
        String token = FundUtils.getToken(request);
        Users user = JwtUtil.getTokenData(token);
        RequestContext.setUser(user);
        RequestContext.setToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.clear();
    }

}

