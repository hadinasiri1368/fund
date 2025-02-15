package org.fund.authentication.permission;

import org.fund.authentication.permission.role.RolePermissionDto;
import org.fund.authentication.permission.user.UserPermissionDto;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Permission;
import org.fund.model.RolePermission;
import org.fund.model.UserPermission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Value("${authentication.paths-to-bypass}")
    private String pathsToBypass;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JpaRepository repository;
    private Map<String, List<Object[]>> ListAllPermissions = null;

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
        List<Object[]> list = this.ListAllPermissions.get(TenantContext.getCurrentTenant());
        if (!FundUtils.isNull(list))
            return list;
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
        list = repository.listByQuery(hql, null);
        this.ListAllPermissions.put(TenantContext.getCurrentTenant(), list);
        return list;
    }

    public void resetPermissionCache() {
        this.ListAllPermissions.remove(TenantContext.getCurrentTenant());
    }

    @Transactional
    public void assignPermissionToRole(List<RolePermissionDto> rolePermissionDtos, Long userId, String uuid) throws Exception {
        List<RolePermission> list;
        for (RolePermissionDto rolePermissionDto : rolePermissionDtos) {
            list = repository.findAll(RolePermission.class).stream()
                    .filter(a -> a.getRole().getId().equals(rolePermissionDto.getRoleId()))
                    .toList();
            repository.batchRemove(list, userId, uuid);
            list = new ArrayList<>();
            for (Permission permission : rolePermissionDto.toPermissions()) {
                list.add(new RolePermission(null, rolePermissionDto.toRole(), permission));
            }
            repository.batchInsert(list, userId, uuid);
        }
    }

    @Transactional
    public void assignPermissionToUser(List<UserPermissionDto> userPermissionDtos, Long userId, String uuid) throws Exception {
        List<UserPermission> list;
        for (UserPermissionDto userPermission : userPermissionDtos) {
            list = repository.findAll(UserPermission.class).stream()
                    .filter(a -> a.getUsers().getId().equals(userPermission.getUserId()))
                    .toList();
            repository.batchRemove(list, userId, uuid);
            list = new ArrayList<>();
            for (Permission permission : userPermission.toPermissions()) {
                list.add(new UserPermission(null, userPermission.toUser(), permission));
            }
            repository.batchInsert(list, userId, uuid);
        }
    }
}
