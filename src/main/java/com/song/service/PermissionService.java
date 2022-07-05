package com.song.service;

import com.song.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.vo.RolePermissionVo;
import com.song.vo.query.PermissionQueryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
public interface PermissionService extends IService<Permission> {


    /**
     * 根据用户id 查询权限菜单列表
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 查询菜单列表
     * @param permissionQueryVo
     * @return
     */
    List<Permission> findPermissionList(PermissionQueryVo permissionQueryVo);



    /**
     * 查询上级菜单列表
     * @return
     */
    List<Permission> findParentPermissionList();
    /**
     * 检查菜单是否有子菜单
     * @param id
     * @return
     */
    boolean hasChildrenOfPermission(Long id);


    /**
     * 查询分配权限树列表
     * @param userId
     * @param roleId
     * @return
     */
    RolePermissionVo findPermissionTree(Long userId, Long roleId);


}
