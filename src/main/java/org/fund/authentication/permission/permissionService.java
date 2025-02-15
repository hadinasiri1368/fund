package org.fund.authentication.permission;

import jakarta.persistence.Query;
import org.apache.catalina.User;
import org.fund.common.FundUtils;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Value("${authentication.paths-to-bypass}")
    private String pathsToBypass;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JpaRepository repository;
    private List<Object[]> ListAllPermissions = null;

    public PermissionService(final JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(Permission permission, Long userId, String uuid) throws Exception {
        repository.save(permission, userId, uuid);
    }

    public void update(Permission permission, Long userId, String uuid) throws Exception {
        repository.update(permission, userId, uuid);
        resetPermissionCache();
    }

    public void delete(Long permissionId, Long userId, String uuid) throws Exception {
        repository.removeById(Permission.class, permissionId, userId, uuid);
        resetPermissionCache();
    }

    public boolean isSensitiveUrl(String requestUrl) {
        List<Permission> permissionList = repository.findAll(Permission.class)
                .stream().filter(a -> !a.getIsSensitive()).toList();
        for (Permission permission : permissionList) {
            if (pathMatcher.match(permission.getUrl(), requestUrl)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBypassedUrl(String requestUrl) {
        String[] paths = pathsToBypass.split(",");
        for (String path : paths) {
            if (pathMatcher.match(path.trim(), requestUrl)) {
                return true;
            }
        }
        return false;
    }

    public void validateUserAccess(Users user, String requestUrl) {
        Permission permission = getPermission(requestUrl);
        Set<Permission> userPermissions = getPermissions(user);
        if (!userPermissions.contains(permission)) {
            throw new FundException(AuthenticationExceptionType.DO_NOT_HAVE_ACCESS_TO_ADDRESS, new Object[]{requestUrl});
        }
    }

    private Permission getPermission(String requestUrl) {
        return repository.findAll(Permission.class).stream()
                .filter(a -> a.getIsSensitive() && requestUrl.toLowerCase().startsWith(a.getUrl().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new FundException(AuthenticationExceptionType.DO_NOT_HAVE_ACCESS_TO_ADDRESS, new Object[]{requestUrl}));
    }

    private Set<Permission> getPermissions(Users user) {
        List<Object[]> list = getAllPermission();
        return list.stream()
                .filter(rec -> FundUtils.longValue(rec[1]).equals(user.getId()))
                .map(rec -> (Permission) rec[0])
                .collect(Collectors.toSet());
    }

    private List<Object[]> getAllPermission() {
        if (!FundUtils.isNull(this.ListAllPermissions))
            return this.ListAllPermissions;
        String hql = "select p,up.user.id userId from userPermission up \n" +
                "    inner join permission p on p.id=up.permission.id \n" +
                "union\n" +
                "select p,ur.user.id userId from userRole ur \n" +
                "    inner join rolePermission rp on rp.role.id=ur.role.id\n" +
                "    inner join permission p on p.id=rp.permission.id\n" +
                "union\n" +
                "select p,ugd.user.id userId from userGroupDetail ugd\n" +
                "    inner join userGroup ug on ug.id=ugd.userGroup.id\n" +
                "    inner join userGroupRole ugr on ugr.userGroup.id=ugd.userGroup.id\n" +
                "    inner join rolePermission rp on rp.role.id=ugr.role.id\n" +
                "    inner join permission p on p.id=rp.permission.id\n";
        this.ListAllPermissions = repository.listByQuery(hql, null);
        return this.ListAllPermissions;
    }

    public void resetPermissionCache() {
        this.ListAllPermissions = null;
    }
}
