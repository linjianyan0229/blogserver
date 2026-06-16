package com.example.blogserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询用户拥有的角色编码
     */
    @Select("SELECT r.code FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<String> selectRoleCodesByUserId(Long userId);

    /**
     * 查询用户拥有的角色
     */
    @Select("SELECT r.* FROM role r JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> selectRolesByUserId(Long userId);
}
