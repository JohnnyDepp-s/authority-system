package com.song.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.song.entity.User;
import com.song.dao.UserMapper;
import com.song.service.FileService;
import com.song.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.utils.SystemConstants;
import com.song.vo.query.UserQueryVo;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private FileService fileService;

    /**
     * 根据用户名 查询 用户信息
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<User> findUserListByPage(IPage<User> page, UserQueryVo userQueryVo) {
        //创建条件构造器对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //部门编号
        queryWrapper.eq(!ObjectUtils.isEmpty(userQueryVo.getDepartmentId()),
                "department_id",userQueryVo.getDepartmentId());
        //用户名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getUsername()),
                "username",userQueryVo.getUsername());
        //真实姓名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getRealName()),
                "real_name",userQueryVo.getRealName());
        //电话
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getPhone()),
                "phone",userQueryVo.getPhone());
        //查询并返回数据
        return baseMapper.selectPage(page,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteById(Long id) {
        //查询
        User user = baseMapper.selectById(id);
        //删除用户角色关系
        baseMapper.deleteUserRole(id);
        //删除用户
        if (baseMapper.deleteById(id) > 0) {
            //判断用户是否存在
            if (user != null && !ObjectUtils.isEmpty(user.getAvatar())
                    && !user.getAvatar().equals(SystemConstants.DEFAULT_AVATAR)) {
                //删除文件
                fileService.deleteFile(user.getAvatar());
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveUserRole(Long userId, List<Long> roleIds) {
        //删除该用户对应的角色信息
        baseMapper.deleteUserRole(userId);
        //保存用户角色信息
        return baseMapper.saveUserRole(userId,roleIds)>0;
    }
}
