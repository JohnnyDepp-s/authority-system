package com.song.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.song.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.vo.query.RoleQueryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
public interface RoleService extends IService<Role> {

    /**
     * 查询角色列表
     * @param page
     * @param roleQueryVo
     */
    IPage<Role> findRoleListByUserId(IPage<Role> page, RoleQueryVo roleQueryVo);

    /**
     * 保存角色权限关系
     * @param roleId
     * @param permissionIds
     * @return
     */
    boolean saveRolePermission(Long roleId, List<Long> permissionIds);

    boolean hashRoleCount(Long id);

    /**
     * 根据用户ID查询该用户拥有的角色ID
     * @param userId
     * @return
     */
    List<Long> findRoleIdByUserId(Long userId);

}
