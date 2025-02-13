package com.bxsbot.console.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IndexDataMapper {

	List<Map<String, Object>> getMsgByEvent(@Param("code")Integer code, @Param("time")String time);

	List<Map<String, Object>> getWebNav(@Param("code")Integer code);

}
