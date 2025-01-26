package org.fund.config.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.constant.Consts;
import org.fund.model.Permission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;


public class RequestContextInterceptor implements HandlerInterceptor {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JpaRepository repository;

    public RequestContextInterceptor(JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isSensitive(request))
            return true;
        String uuid = request.getAttribute(Consts.HEADER_UUID_PARAM_NAME).toString();
        String token = FundUtils.getToken(request);
        Users user = JwtUtil.getTokenData(token);
        RequestContext.setUuid(uuid);
        RequestContext.setUser(user);
        RequestContext.setToken(token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.clear();
    }

    private boolean isSensitive(HttpServletRequest request) {
        List<String> excludePathPatterns = new ArrayList<>();
        List<Permission> permissionList = repository.findAll(Permission.class)
                .stream().filter(a -> !a.getIsSensitive()).toList();
        for (Permission permission : permissionList) {
            if (pathMatcher.match(permission.getUrl(), request.getRequestURI())) {
                return false;
            }
        }
        return true;
    }

}

