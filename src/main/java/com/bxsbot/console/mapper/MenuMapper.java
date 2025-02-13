package com.bxsbot.console.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bxsbot.console.bean.Menu;
@Mapper
public interface MenuMapper {
	 List<Menu> getAllMenus();
}
