package org.fund.authentication.user;

import org.fund.authentication.permission.PermissionService;
import org.fund.authentication.user.dto.UserGroupDetailDto;
import org.fund.authentication.user.dto.UserPermissionDto;
import org.fund.authentication.user.dto.UserRoleDto;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final JpaRepository repository;

    public UserService(JpaRepository repository) {
        this.repository = repository;
    }

    public void insert(Users users, Long userId, String uuid) throws Exception {
        repository.save(users, userId, uuid);
    }

    public void update(Users users, Long userId, String uuid) throws Exception {
        repository.update(users, userId, uuid);
    }

    public void delete(Long userId, Long currentUerId, String uuid) throws Exception {
        repository.removeById(UserGroup.class, userId, currentUerId, uuid);
    }


    public void insertUserGroup(UserGroup userGroup, Long userId, String uuid) throws Exception {
        repository.save(userGroup, userId, uuid);
    }

    public void updateUserGroup(UserGroup userGroup, Long userId, String uuid) throws Exception {
        repository.update(userGroup, userId, uuid);
    }

    public void deleteUserGroup(Long userGroupId, Long userId, String uuid) throws Exception {
        repository.removeById(UserGroup.class, userGroupId, userId, uuid);
    }

    @Transactional
    public void assignUserToGroup(List<UserGroupDetailDto> userPermissionDtos, Long userId, String uuid) throws Exception {
        List<UserGroupDetail> list;
        for (UserGroupDetailDto userGroupDetailDto : userPermissionDtos) {
            list = repository.findAll(UserGroupDetail.class).stream()
                    .filter(a -> a.getUserGroup().getId().equals(userGroupDetailDto.getUserGroupId()))
                    .toList();
            repository.batchRemove(list, userId, uuid);
            list = new ArrayList<>();
            for (Users user : userGroupDetailDto.toUsers()) {
                list.add(new UserGroupDetail(null, user, userGroupDetailDto.toUserGroup()));
            }
            repository.batchInsert(list, userId, uuid);
        }
    }

    @Transactional
    public void assignRoleToUser(List<UserRoleDto> userRoleDtos, Long userId, String uuid) throws Exception {
        List<UserRole> list;
        for (UserRoleDto userRoleDto : userRoleDtos) {
            list = repository.findAll(UserRole.class).stream()
                    .filter(a -> a.getUser().getId().equals(userRoleDto.getUserId()))
                    .toList();
            repository.batchRemove(list, userId, uuid);
            list = new ArrayList<>();
            for (Role role : userRoleDto.toEntityList(Role.class, repository)) {
                list.add(new UserRole(null, userRoleDto.toEntity(Users.class, repository), role));
            }
            repository.batchInsert(list, userId, uuid);
        }
    }

    @Transactional
    public void assignPermissionToUser(List<UserPermissionDto> userPermissionDtos, Long userId, String uuid) throws Exception {
        List<UserPermission> list;
        for (UserPermissionDto userPermission : userPermissionDtos) {
            list = repository.findAll(UserPermission.class).stream()
                    .filter(a -> a.getUser().getId().equals(userPermission.getUserId()))
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
