package com.song.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.song.dto.RolePermissionDTO;
import com.song.entity.Role;
import com.song.service.PermissionService;
import com.song.service.RoleService;
import com.song.utils.Result;
import com.song.vo.RolePermissionVo;
import com.song.vo.query.RoleQueryVo;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询角色列表
     * @param roleQueryVo
     * @return
     */
    @GetMapping("/list")
    public Result list(RoleQueryVo roleQueryVo){
        //创建分页对象
        IPage<Role> page =new Page<Role>(roleQueryVo.getPageNo(),roleQueryVo.getPageSize());
        //调用分页查询方法
        roleService.findRoleListByUserId(page,roleQueryVo);
        //返回数据
        return Result.ok(page);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role role){
        if(roleService.save(role)){
            return Result.ok().message("角色添加成功");
        }
        return Result.error().message("角色添加失败");
    }
    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Role role){
        if(roleService.updateById(role)){
            return Result.ok().message("角色修改成功");
        }
        return Result.error().message("角色修改失败");
    }
    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        if(roleService.removeById(id)){
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("角色删除失败");
    }

    /**
     * 分配权限-查询权限树数据
     * @param userId
     * @param roleId
     * @return
     */
    @GetMapping("/getAssignPermissionTree")
    public Result getAssignPermissionTree(Long userId, Long roleId) {
        //调用查询权限树数据的方法
        RolePermissionVo permissionTree = permissionService.findPermissionTree(userId, roleId);
        //返回数据
        return Result.ok(permissionTree);
    }

    /**
     * 分配权限-保存权限数据
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/saveRoleAssign")
    public Result saveRoleAssign(@RequestBody RolePermissionDTO rolePermissionDTO) {
        if (roleService.saveRolePermission(rolePermissionDTO.getRoleId(),
                rolePermissionDTO.getList())) {
            return Result.ok().message("权限分配成功");
        } else {
            return Result.error().message("权限分配失败");
        }
    }

    /**
     * 检查用户是否被使用
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id){
        System.out.println("hello");
        //查看该角色是否被使用
        if (roleService.hashRoleCount(id)){
            return Result.exist().message("该角色已分配给其他用户使用,无法删除");
        }
        return Result.ok();
    }


}

