package com.song.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.song.dao.UserMapper;
import com.song.entity.Role;
import com.song.dao.RoleMapper;
import com.song.entity.User;
import com.song.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.vo.query.RoleQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Resource
    private UserMapper userMapper;


    /**
     * 根据用户ID查询 角色列表
     * @param page
     * @param roleQueryVo
     */
    @Override
    public IPage<Role> findRoleListByUserId(IPage<Role> page, RoleQueryVo roleQueryVo) {
        //创建条件构造器
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        //角色名称
        queryWrapper.like(!ObjectUtils.isEmpty(roleQueryVo.getRoleName()),
                "role_name",roleQueryVo.getRoleName());
        //排序
        queryWrapper.orderByAsc("id");
        //根据用户ID查询用户信息
        User user = userMapper.selectById(roleQueryVo.getUserId());
        //如果用户不为空、且不是管理员，则只能查询自己创建的角色
        if(user!=null && !ObjectUtils.isEmpty(user.getIsAdmin()) && user.getIsAdmin() !=1){
            queryWrapper.eq("create_user",roleQueryVo.getUserId());
        }
        return baseMapper.selectPage(page,queryWrapper);
    }

    /**
     * 保存角色权限关系
     * @param roleId
     * @param permissionIds
     * @return
     */
    @Override
    public boolean saveRolePermission(Long roleId, List<Long> permissionIds) {
        //删除该角色对应的权限信息
        baseMapper.deleteRolePermission(roleId);
        //保存角色权限
        return baseMapper.saveRolePermission(roleId,permissionIds)>0;
    }

    @Override
    public boolean hashRoleCount(Long id) {
        return baseMapper.getRoleCountByRoleId(id)>0;
    }

    /**
     * 根据用户ID查询该用户拥有的角色
     * @param userId
     * @return
     */
    @Override
    public List<Long> findRoleIdByUserId(Long userId) {
        return baseMapper.findRoleIdByUserId(userId);
    }
}
