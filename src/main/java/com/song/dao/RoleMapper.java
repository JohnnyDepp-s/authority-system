package com.song.dao;

import com.song.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 删除角色权限关系
     * @param roleId
     */
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermission(Long roleId);

    /**
     * 保存角色权限关系
     * @param roleId
     * @param permissionIds
     * @return
     */
    int saveRolePermission(Long roleId, List<Long> permissionIds);



    @Select("select count(1) from sys_user_role where role_id = #{roleId}")
    int getRoleCountByRoleId(Long roleId);

    /**
     * 根据用户ID查询该用户拥有的角色ID
     * @param userId
     * @return
     */
    @Select("select role_id from `sys_user_role` where user_id = #{userId}")
    List<Long> findRoleIdByUserId(Long userId);
}
