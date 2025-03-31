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

	/***
	 * 添加暂停几秒
	 * @param pause 
	 * @param itemSort 
	 * @param lmId 
	 * @param pause
	 */
	void saveLMPause(@Param("itemName")String itemName, @Param("itemSort")Integer itemSort, @Param("s")Integer s, @Param("lmId")Integer lmId);
	/***
	 * 传入排序，后面+1
	 * @param lmId
	 * @param itemSort
	 * @param num 
	 */
		
	void updateAutoSort( @Param("lmId")Integer lmId,  @Param("itemSort")Integer itemSort,@Param("num") Integer num);

}
