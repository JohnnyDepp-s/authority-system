package com.song.service;

import com.song.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
