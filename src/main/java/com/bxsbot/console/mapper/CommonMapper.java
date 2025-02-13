package com.bxsbot.console.mapper;

import java.util.List;
import java.util.Map;

public interface CommonMapper {
	     
	      List<Map<String,Object>> selectByPage(Map<String, Object> paramMap);
	      Integer selectByPageNum(Map<String, Object> paramMap);
	      void update(Map<String, Object> paramMap);
	      void save(Map<String, Object> paramMap);
}
