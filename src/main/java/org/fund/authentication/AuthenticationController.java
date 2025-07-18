package org.fund.authentication;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.authentication.otp.dto.OtpRequestDto;
import org.fund.authentication.permission.PermissionDto;
import org.fund.authentication.permission.PermissionService;
import org.fund.authentication.permission.role.RoleDto;
import org.fund.authentication.permission.role.RolePermissionDto;
import org.fund.authentication.permission.role.RoleUserGroupDto;
import org.fund.authentication.user.UserService;
import org.fund.authentication.user.dto.*;
import org.fund.config.dataBase.TenantContext;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.model.Permission;
import org.fund.model.Role;
import org.fund.model.UserGroup;
import org.fund.model.Users;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Validated
public class AuthenticationController {
    private final AuthenticationService service;
    private final PermissionService permissionService;
    private final UserService userService;
    private final GenericDtoMapper mapper;

    public AuthenticationController(AuthenticationService service
            , PermissionService permissionService
            , GenericDtoMapper mapper
            , UserService userService) {
        this.service = service;
        this.permissionService = permissionService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody LoginDto loginDto) throws Exception {
        return service.login(loginDto);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/getOtpStrategies")
    public Map<Integer, String> getOtpStrategies() {
        return service.getOtpStrategyTypeList().stream()
                .collect(Collectors.toMap(
                        item -> item.getId(),
                        OtpStrategyType::getTitle
                ));
    }

    @PostMapping(path = Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/sendOtpForLogin")
    public void sendOtpForLogin(@RequestBody OtpRequestDto otpRequestDto) {
        service.sendOtpForLogin(otpRequestDto);
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role/add")
    public void insertRole(@RequestBody RoleDto roleDto) throws Exception {
        permissionService.insertRole(mapper.toEntity(Role.class, roleDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/refreshToken")
    public String refreshToken() throws Exception {
        return service.refreshToken(TenantContext.getCurrentTenant(), RequestContext.getToken());
    }

    @PutMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role/edit")
    public void updateRole(@RequestBody RoleDto roleDto) throws Exception {
        permissionService.updateRole(mapper.toEntity(Role.class, roleDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role/remove/{id}")
    public void deleteRole(@PathVariable("id") @NotEmpty(fieldName = "roleId") Long roleId) throws Exception {
        permissionService.deleteRole(roleId, RequestContext.getUserId(), RequestContext.getUuid());
    }


    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/add")
    public void insertPermission(@RequestBody PermissionDto permissionDto) throws Exception {
        permissionService.insert(mapper.toEntity(Permission.class, permissionDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/edit")
    public void updatePermission(@RequestBody PermissionDto permissionDto) throws Exception {
        permissionService.update(mapper.toEntity(Permission.class, permissionDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/remove/{id}")
    public void deletePermission(@PathVariable("id") @NotEmpty(fieldName = "permissionId") Long permissionId) throws Exception {
        permissionService.delete(permissionId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/{id}")
    public Permission getPermission(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Permission.class) Long id) {
        List<Permission> permissionList = permissionService.listPermission(id);
        return permissionList.get(0);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission")
    public List<Permission> getPermission() {
        return permissionService.listPermission(null);
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/add")
    public void insertUser(@RequestBody UserDto userDto) throws Exception {
        userService.insert(mapper.toEntity(Users.class, userDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/edit")
    public void updateUser(@RequestBody UserDto userDto) throws Exception {
        userService.update(mapper.toEntity(Users.class, userDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/remove")
    public void deleteUser(@NotEmpty(fieldName = "userId") Long userId) throws Exception {
        userService.delete(userId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup/add")
    public void insertUserGroup(@RequestBody UserGroupDto userGroupDto) throws Exception {
        userService.insertUserGroup(mapper.toEntity(UserGroup.class, userGroupDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup/edit")
    public void updateUserGroup(@RequestBody UserGroupDto userGroupDto) throws Exception {
        userService.updateUserGroup(mapper.toEntity(UserGroup.class, userGroupDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup/remove")
    public void deleteUserGroup(@NotEmpty(fieldName = "userGroupId") Long userGroupId) throws Exception {
        userService.deleteUserGroup(userGroupId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/assignPermissionToRole")
    public void assignPermissionToRole(@RequestBody List<RolePermissionDto> rolePermissionDtos) throws Exception {
        permissionService.assignPermissionToRole(rolePermissionDtos, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/permission/assignRoleToUserGroup")
    public void assignRoleToUserGroup(@RequestBody List<RoleUserGroupDto> roleUserGroupDtos) throws Exception {
        permissionService.assignRoleToUserGroup(roleUserGroupDtos, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/assignUserToGroup")
    public void assignUserToGroup(@RequestBody List<UserGroupDetailDto> userGroupDetailDtos) throws Exception {
        userService.assignUserToGroup(userGroupDetailDtos, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/assignRoleToUser")
    public void assignRoleToUser(@RequestBody List<UserRoleDto> userRoleDtos) throws Exception {
        userService.assignRoleToUser(userRoleDtos, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PostMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/assignPermissionToUser")
    public void assignPermissionToUser(@RequestBody List<UserPermissionDto> userPermissionDtos) throws Exception {
        userService.assignPermissionToUser(userPermissionDtos, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/{id}")
    public Users getUser(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Users.class) Long id) {
        List<Users> users = userService.listUsers(id);
        return users.get(0);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user")
    public List<Users> getUser() {
        return userService.listUsers(null);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/userRolePerUser/{userId}")
    public UserRoleDto userRoles(@PathVariable Long userId) {
        return userService.findUserRole(userId);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role")
    public List<Role> getRole() {
        return permissionService.listRole(null);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role/{id}")
    public Role getRole(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = Role.class) Long id) {
        List<Role> roleList = permissionService.listRole(id);
        return roleList.getFirst();
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup/{id}")
    public UserGroup getUserGroup(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = UserGroup.class) Long id) {
        List<UserGroup> userGroupList = userService.listUserGroup(id);
        return userGroupList.getFirst();
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup")
    public List<UserGroup> getUserGroup() {
        return userService.listUserGroup(null);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/userGroup/userGroupRolePerUserGroup/{userGroupId}")
    public RoleUserGroupDto userGroupRoles(@PathVariable Long userGroupId) {
        return userService.findUserGroupRole(userGroupId);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/role/rolePermissionPerRoleId/{roleId}")
    public RolePermissionDto rolePermissions(@PathVariable Long roleId) {
        return permissionService.findRolePermission(roleId);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/userPermissionPerUserId/{userId}")
    public UserPermissionDto getUserPermissions(@PathVariable Long userId){
        return userService.findUserPermission(userId);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/authentication/user/userGroupPerUserId/{userId}")
    public UserGroupDetailDto getUserGroupDetails(@PathVariable Long userId){
        return userService.findUserGroupDetail(userId);
    }

}
