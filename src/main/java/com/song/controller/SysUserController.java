package com.song.controller;

import com.song.config.redis.RedisService;
import com.song.entity.Permission;
import com.song.entity.User;
import com.song.entity.UserInfo;
import com.song.utils.JwtUtils;
import com.song.utils.MenuTree;
import com.song.utils.Result;
import com.song.vo.RouterVo;
import com.song.vo.TokenVo;
import com.sun.deploy.net.HttpResponse;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {


    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisService redisService;

    /**
     * 刷新token信息
     *
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        //从headers请求头中获取token信息
        String token = request.getHeader("token");
        //判断headers头部是否含有token信息
        if (ObjectUtils.isEmpty(token)) {
            //从请求参数当中获取token
            token = request.getParameter("token");
        }
        //从 Spring Security上下文中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户身份信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //定义变量，保存新的token信息
        String newToken = "";
        //验证提交过来的token信息是否合法
        if (jwtUtils.validateToken(token, userDetails)) {
            //重新生成新的token
            newToken = jwtUtils.refreshToken(token);
        }
        //获取本次token的到期时间，交给前端做判断
        long expireTime = Jwts.parser().setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(newToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        //清除原来的token信息
        String oldTokenKey = "token_" + token;
        redisService.del(oldTokenKey);
        //存储新的token
        String newTokenKey = "token_" + newToken;
        //保存到redis缓存当中
        redisService.set(newTokenKey, newToken, jwtUtils.getExpiration() / 1000);
        //创建TokenVo对象
        TokenVo tokenVo = new TokenVo(expireTime, newToken);
        //返回数据
        return Result.ok(tokenVo).message("token生成成功");

    }


    /**
     * 获取登录用户信息
     *
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //判断身份信息authentication 是否为空
        if (authentication == null) {
            return Result.error().message("用户信息查询失败");
        }
        //获取用户信息
        User user = (User) authentication.getPrincipal();

        //获取该用户拥有的角色权限信息
        List<Permission> permissionList = user.getPermissionList();
        //获取权限编码
        Object[] roles = permissionList.stream()
                .filter(Objects::nonNull)
                .map(Permission::getCode).toArray();

        //创建用户信息
        UserInfo userInfo = new UserInfo(user.getId(), user.getNickName(), user.getAvatar(), null, roles);
        return Result.ok(userInfo).message("用户信息查询成功");
    }

    /**
     * 获取菜单数据
     *
     * @return
     */
    @GetMapping("/getMenuList")
    public Result getMenuList() {
        //从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户信息
        User user = (User) authentication.getPrincipal();
        //获取相应的权限
        List<Permission> permissionList = user.getPermissionList();
        //筛选目录和菜单
        List<Permission> collect = permissionList.stream()
                .filter(item -> item != null && item.getType() !=2)
                .collect(Collectors.toList());
        //生成路由数据
        List<RouterVo> routerVoList = MenuTree.makeRouter(collect, 0L);
        //返回数据
        return Result.ok(routerVoList).message("菜单数据获取成功");
    }

    /**
     * 退出请求
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public Result layout(HttpServletRequest request, HttpServletResponse response){
        //从头部当中获取token信息
        String token = request.getHeader("token");
        //如果请求头部当中没有token
        if (ObjectUtils.isEmpty(token)){
            //从请求参数当中获取token
            token = request.getParameter("token");
        }
        //Spring Security 上下文 获取用户相关信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //清空用户信息
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            //清空redis里面的token
            String key = "token_" + token;
            redisService.del(key);
        }
        return Result.ok().message("用户退出成功");
    }
}
