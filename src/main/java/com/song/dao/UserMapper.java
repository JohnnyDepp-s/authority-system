package com.song.dao;

import com.song.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 删除用户角色关系
     * @param userId
     * @return
     */
    @Delete("delete from sys_user_role where user_id=#{userId}")
    int deleteUserRole(Long userId);

    /**
     * 保存用户角色关系
     * @param userId
     * @param roleIds
     * @return
     */
    int saveUserRole(Long userId, List<Long> roleIds);

}
