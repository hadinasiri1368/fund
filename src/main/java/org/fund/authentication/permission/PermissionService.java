package org.fund.authentication.permission;

import org.fund.authentication.permission.role.RolePermissionDto;
import org.fund.authentication.permission.role.RoleUserGroupDto;
import org.fund.authentication.user.UserService;
import org.fund.authentication.user.dto.UserPermissionDto;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Value("${authentication.paths-to-bypass}")
    private String pathsToBypass;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JpaRepository repository;
    private Map<String, List<Object[]>> listAllPermissions;
    private final UserService userService;

    public PermissionService(JpaRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
        this.listAllPermissions = new HashMap<>();
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
        if (user.getIsAdmin())
            return;
        Permission permission = getPermission(requestUrl);
        Set<Permission> userPermissions = getPermissions(user);
        if (!userPermissions.contains(permission)) {
            throw new FundException(AuthenticationExceptionType.DO_NOT_HAVE_ACCESS_TO_ADDRESS, new Object[]{requestUrl});
        }
    }

    private Permission getPermission(String requestUrl) {
        return repository.findAll(Permission.class).stream()
                .filter(a -> a.getIsSensitive() && FundUtils.removeNumericPathVariables(requestUrl).toLowerCase().startsWith(a.getUrl().toLowerCase()))
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
        List<Object[]> list = this.listAllPermissions.get(TenantContext.getCurrentTenant());
        if (!FundUtils.isNull(list))
            return list;
        String hql = "select p,up.user.id userId from userPermission up \n" +
                "    inner join permission p on p.id=up.permission.id \n";
        list = repository.listObjectByQuery(hql, null);


        hql = "select p,ur.user.id userId from userRole ur \n" +
                "    inner join rolePermission rp on rp.role.id=ur.role.id\n" +
                "    inner join permission p on p.id=rp.permission.id\n";
        list.addAll(repository.listObjectByQuery(hql, null));


        hql = "select p,ugd.user.id userId from userGroupDetail ugd\n" +
                "    inner join userGroup ug on ug.id=ugd.userGroup.id\n" +
                "    inner join userGroupRole ugr on ugr.userGroup.id=ugd.userGroup.id\n" +
                "    inner join rolePermission rp on rp.role.id=ugr.role.id\n" +
                "    inner join permission p on p.id=rp.permission.id\n";
        list.addAll(repository.listObjectByQuery(hql, null));


        this.listAllPermissions.put(TenantContext.getCurrentTenant(), list);
        return list;
    }

    public void resetPermissionCache() {
        this.listAllPermissions.remove(TenantContext.getCurrentTenant());
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
        userService.assignPermissionToUser(userPermissionDtos, userId, uuid);
    }

    public void insertRole(Role role, Long userId, String uuid) throws Exception {
        repository.save(role, userId, uuid);
    }

    public void updateRole(Role role, Long userId, String uuid) throws Exception {
        repository.update(role, userId, uuid);
    }

    public void deleteRole(Long roleId, Long userId, String uuid) throws Exception {
        repository.removeById(Role.class, roleId, userId, uuid);
    }

    @Transactional
    public void assignRoleToUserGroup(List<RoleUserGroupDto> rolePermissionDtos, Long userId, String uuid) throws Exception {
        List<UserGroupRole> list;
        for (RoleUserGroupDto roleUserGroupDto : rolePermissionDtos) {
            list = repository.findAll(UserGroupRole.class).stream()
                    .filter(a -> a.getUserGroup().getId().equals(roleUserGroupDto.getUserGroupId()))
                    .toList();
            repository.batchRemove(list, userId, uuid);
            list = new ArrayList<>();
            for (Role role : roleUserGroupDto.toRoles()) {
                list.add(new UserGroupRole(null, role, roleUserGroupDto.toUserGroup()));
            }
            repository.batchInsert(list, userId, uuid);
        }
    }
}
