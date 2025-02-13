package com.bxsbot.console.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bxsbot.console.bean.Menu;
import com.bxsbot.console.mapper.MenuMapper;
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 获取构建好的菜单树
     */
    public List<Menu> getMenuTree() {
        List<Menu> allMenus = menuMapper.getAllMenus(); // 从数据库获取所有菜单
        return buildMenuTree(allMenus, 0); // 构建菜单树，0 表示顶级菜单的 pid
    }

    /**
     * 递归构建菜单树
     */
    private List<Menu> buildMenuTree(List<Menu> menus, int parentId) {
        List<Menu> tree = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getPid() == parentId && menu.getIsMenu() == 1) {
                menu.setSubmenus(buildMenuTree(menus, menu.getId())); // 递归设置子菜单
                tree.add(menu);
            }
        }
        return tree;
    }
}
