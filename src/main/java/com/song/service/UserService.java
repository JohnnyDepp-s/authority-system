package com.song.service;

import com.song.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    public User findUserByUserName(String userName);

}
