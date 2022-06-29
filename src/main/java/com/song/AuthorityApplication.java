package com.song;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 宋超
 * HP - the login name of the current user.
 * 2022/6/29 - the current system date.
 * 11:42 - the current system time.
 */
@MapperScan("com.song.dao")
@SpringBootApplication
public class AuthorityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorityApplication.class,args);
    }
}
