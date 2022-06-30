package com.song.config.security.service;

import com.song.entity.Permission;
import com.song.entity.User;
import com.song.service.PermissionService;
import com.song.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  by Song 2022-06-30
 * 用户认证处理类
 */
@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;


    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        User user = userService.findUserByUserName(username);
        //若用户为空
        if (user == null ){
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        //查询当前登录用户拥有的权限列表
        List<Permission> permissionList = permissionService.findPermissionListByUserId(user.getId());
        //获取对应的权限编码
        List<String> codeList = permissionList.stream()
                .filter(Objects::nonNull)
                .map(Permission::getCode)
                .filter(item -> item != null)
                .collect(Collectors.toList());
        //将权限编码列表转换成数组
        String[] strings = codeList.toArray(new String[codeList.size()]);
        //设置权限列表
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
        //将权限列表设置user对象
        user.setAuthorities(authorityList);
        //设置该用户的菜单新
        user.setPermissionList(permissionList);
        //查询成功
        return user;
    }
}
