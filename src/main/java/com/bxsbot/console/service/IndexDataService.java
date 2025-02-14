package com.bxsbot.console.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.mapper.IndexDataMapper;
import com.bxsbot.console.mapper.TjMapper;
import com.bxsbot.console.utils.DateUtils;

@Service
public class IndexDataService {

	@Autowired
	private IndexDataMapper indexDataMapper;
	@Autowired
    private TjService service;

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

	public List getData(Integer code) {
		Map<String,Object> map=new ConcurrentHashMap<String, Object>();
		if(1==code) {
		
			map.put("cc", "total_price");
			
		}else if (2==code) {
			map.put("cc", "vol1");
		}else if (3==code) {
			map.put("cc", "price_change_percentage");
		}else if (4==code) {
			//map.put("cc", "vol1");
		}else if (5==code) {
			map.put("cc", "pr1");
		}else if (6==code) {
		//成交量上升，价格下降
		}else if (7==code) {
			//卖出比
			map.put("cc", "buy_sell_ratio");
			}
		map.put("cca", "desc");
		return service.selectByPageIndex(map);
	}
	
	
}
