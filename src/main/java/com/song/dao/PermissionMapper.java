package com.song.dao;

import com.song.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {


    /**
     *根据用户id 查询权限菜单列表
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 根据角色ID查询权限列表
     * @param roleId
     * @return
     */
    List<Permission> findPermissionListByRoleId(Long roleId);

}
