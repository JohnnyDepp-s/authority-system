package com.song.vo.query;

import com.song.entity.User;
import lombok.Data;

/**
 * @author song
 * 2022/7/4
 */
@Data
public class UserQueryVo extends User {
    private Long pageNo = 1L;//当前页码
    private Long pageSize = 10L;//每页显示数量
}

