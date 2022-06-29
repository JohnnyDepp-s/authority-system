package com.song.controller;


import com.song.service.UserService;
import com.song.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author song
 * @since 2022-06-29
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getList")
    public Result getList(){
        return Result.ok(userService.list());
    }
}

