package com.example.blogserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.entity.Permission;
import com.example.blogserver.mapper.PermissionMapper;
import com.example.blogserver.vo.PermissionTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务（权限配置页面）
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionMapper permissionMapper;

    /**
     * 全部权限（平铺）
     */
    public List<Permission> listAll() {
        return permissionMapper.selectList(Wrappers.<Permission>lambdaQuery().orderByAsc(Permission::getSort));
    }

    /**
     * 权限树（用于权限配置页面勾选）
     */
    public List<PermissionTreeVO> listTree() {
        List<Permission> all = listAll();
        List<PermissionTreeVO> nodes = all.stream().map(p -> {
            PermissionTreeVO vo = new PermissionTreeVO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildTree(nodes, 0L);
    }

    private List<PermissionTreeVO> buildTree(List<PermissionTreeVO> all, Long parentId) {
        List<PermissionTreeVO> children = new ArrayList<>();
        for (PermissionTreeVO node : all) {
            Long pid = node.getParentId() == null ? 0L : node.getParentId();
            if (pid.equals(parentId)) {
                node.setChildren(buildTree(all, node.getId()));
                children.add(node);
            }
        }
        return children;
    }
}
