package com.bxsbot.console.mapper;

import org.apache.ibatis.annotations.Param;

import com.bxsbot.console.bean.ZiXun;

/***
 * 
 */
public interface UtilMapper {

	ZiXun selectZxTextById(@Param("id") Integer id);

	void saveZxText(ZiXun ziXun);

	void updateZxTextById(ZiXun ziXun);

}
