package com.bxsbot.console.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bxsbot.console.mapper.IndexDataMapper;
import com.bxsbot.console.utils.DateUtils;

@Service
public class IndexDataService {

	@Autowired
	private IndexDataMapper indexDataMapper;

	public List<Map<String, Object>> getMsg(Integer code) {
		if(null!=code) {
			String time=DateUtils.getDate();
			return indexDataMapper.getMsgByEvent(code,time);
		}
		return null;
	}

	public  List<Map<String, Object>> getWebNav(Integer code) {
		return indexDataMapper.getWebNav(code);
	}
	
	
}
