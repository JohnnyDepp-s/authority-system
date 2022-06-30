package com.song.controller;

import com.song.config.redis.RedisService;
import com.song.utils.JwtUtils;
import com.song.utils.Result;
import com.song.vo.TokenVo;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {


    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisService redisService;

    /**
     * 刷新token信息
     * @param request
     * @return
     */
    @PostMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request){
        //从headers请求头中获取token信息
        String token = request.getHeader("token");
        //判断headers头部是否含有token信息
        if(ObjectUtils.isEmpty(token)){
            //从请求参数当中获取token
            token= request.getParameter("token");
        }
        //从 Spring Security上下文中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户身份信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //定义变量，保存新的token信息
        String newToken = "";
        //验证提交过来的token信息是否合法
        if (jwtUtils.validateToken(token,userDetails)){
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
}
