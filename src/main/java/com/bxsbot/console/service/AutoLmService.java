package com.bxsbot.console.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.dao.CommonDao;
import com.bxsbot.console.mapper.UtilMapper;
import com.bxsbot.console.utils.MapUtils;
import com.bxsbot.console.utils.PageUtils;
import com.bxsbot.console.utils.Stringutils;

@Service 
public class AutoLmService {
	 @Autowired
		private CommonDao commonDao;
	 @Autowired
	 private UtilMapper utilMapper;
	 /***
	  * 保存或者修改
	  * 根据前端传入参数修改，有id修改，没有新增
	  * @param webpa
	  * @param rp
	  */
	 @Transactional
	 public void save(Map<String, Object> webpa,ReturnPage rp) {
			Page page = rp.getPage();
			if(null!=page) {
				Integer pageId=page.getId();
				if(null!=page.getPid()) {
					pageId=page.getPid();
				}
				Integer  id=MapUtils.getValueInt(webpa, "id");
				PageUtils.setInfo(pageId, rp);
				if(null==id) {
					commonDao.save(webpa,rp);
					Integer pause=MapUtils.getValueInt(webpa, "pause");
					if(null!=pause) {
						//添加暂停秒
						String itemName="暂停"+pause+"S";
						Integer itemSort=MapUtils.getValueInt(webpa, "itemSort")+1;
						Integer lmId=MapUtils.getValueInt(webpa, "lmId");
						utilMapper.saveLMPause(itemName,itemSort,pause,lmId);
					}
				}else {
					commonDao.update(webpa,rp);
				}
				
				if(webpa.containsKey("autoSort")   && null!=webpa.get("autoSort")) {
					//序号自动排序
					String au=MapUtils.getValueStr(webpa, "autoSort");
					if(null!=au && au.length()>0) {
						Integer lmId = MapUtils.getValueInt(webpa, "lmId");
						Integer itemSort = MapUtils.getValueInt(webpa, "itemSort");
						if(null!=lmId && null!=itemSort) {
							utilMapper.updateAutoSort(lmId,itemSort);
						}
					}
				}
			}
	 }
}
