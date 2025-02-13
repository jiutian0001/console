package com.bxsbot.console.mapper;

import java.util.List;
import java.util.Map;

public interface TjMapper {

	List<Map<String,Object>> selectByPage(Map<String, Object> webpa);

	Integer getTotalNum(Map<String, Object> webpa);

}
