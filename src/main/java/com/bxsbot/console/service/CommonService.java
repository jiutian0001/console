package com.bxsbot.console.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.dao.CommonDao;
import com.bxsbot.console.utils.MapUtils;
import com.bxsbot.console.utils.PageUtils;
/***
 * 业务处理公用
 */
@Service
public class CommonService {

	 @Autowired
	private CommonDao commonDao;
	


	 /***
	  * 保存或者修改
	  * 根据前端传入参数修改，有id修改，没有新增
	  * @param webpa
	  * @param rp
	  */
	 public void save(Map<String, Object> webpa,ReturnPage rp) {
			Page page = rp.getPage();
			if(null!=page) {
				//String tableName=MapUtils.getValueStr(rp.getVal(), "tableName");
				//webpa.put("tableName", tableName);
				Integer pageId=page.getId();
				if(null!=page.getPid()) {
					pageId=page.getPid();
				}
				Integer  id=MapUtils.getValueInt(webpa, "id");
				PageUtils.setInfo(pageId, rp);
				if(null==id) {
					commonDao.save(webpa,rp);
				}else {
					commonDao.update(webpa,rp);
				}
			}
	 }

	/***
	 * 列表
	 * @param parameterMap
	 * @return
	 */
	public void selectByPage(Map<String, Object> webpa,ReturnPage rp) {
		Page page = rp.getPage();
		if(null!=page) {
			Integer pageId=page.getId();
			PageUtils.setSearch(pageId, rp);
			PageUtils.setTable(pageId, rp);
			PageUtils.setInfo(pageId, rp);
			//String tableName=MapUtils.getValueStr(rp.getVal(), "tableName");
			//webpa.put("tableName", tableName);
			
			commonDao.selectByPage(webpa, rp);
		}
	}
	/***
	 * 条件查询
	 * @param webpa
	 * @param rp
	 * @return
	 */
	public void findBySearch(Map<String, Object> webpa,ReturnPage rp) {
		Page page = rp.getPage();
		Integer  id=MapUtils.getValueInt(webpa, "id");
		if(null!=page) {
			Integer pageId=page.getId();
			if(null!=page.getPid()) {
				pageId=page.getPid();
			}
			PageUtils.setInfo(pageId, rp);
			//String tableName=MapUtils.getValueStr(rp.getVal(), "tableNamePar");
		//	webpa.put("tableNamePar", tableName);
			if(id!=null) {
				commonDao.findBySearch(webpa, rp);
			}else {
				rp.getVal().put("map", new HashMap<String, Object>());
			}
			//set配置
			
		}
	}
	

}
