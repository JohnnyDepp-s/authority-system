package com.song.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.song.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.vo.query.RoleQueryVo;

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
}
