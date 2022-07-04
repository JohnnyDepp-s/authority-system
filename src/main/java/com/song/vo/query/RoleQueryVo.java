package com.song.vo.query;


import com.song.entity.Role;
import lombok.Data;

/**
 * @author song
 * 2022/7/4
 */
@Data
public class RoleQueryVo extends Role {
    private Long pageNo = 1L;//当前页码
    private Long pageSize = 10L;//每页显示数量
    private Long userId;//用户ID
}
