package com.bxsbot.console.dao;

import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.PageBean;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.mapper.CommonMapper;
import com.bxsbot.console.utils.CacheMaps;
import com.bxsbot.console.utils.MapUtils;
@Repository
/***
 * 通用dao
 */
public class CommonDao {
	@Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private CommonMapper commonMapper;
	



	/***
	 * 条件查询 返回map
	 * 页面搜索的才可以查询
	 */
	public void findBySearch(Map<String, Object> webpa, ReturnPage rp) {
		String tableName=getTableName(rp.getVal());
		StringBuffer buffer=new StringBuffer();
		buffer.append(CacheMaps.SELECT).append(getColumnsAS(rp)).append(CacheMaps.FROM).append(tableName).append(CacheMaps.WHERE);
		String sea= getSearchAll(webpa,rp);
		buffer.append(sea);
		webpa.put("sql", buffer.toString());
		List<Object> selectList = sqlSessionTemplate.selectList("com.bxsbot.console.mapper.CommonMapper.selectByPage", webpa);
		if(null!=selectList && selectList.size()>0) {
			rp.getVal().put("map", selectList.get(0));
		}
		
	}
	/***
	 * 传什么参数根据什么查询
	 * @param webpa
	 * @param rp
	 * @return
	 */
	private String getSearchAll(Map<String, Object> webpa, ReturnPage rp) {
		if(webpa.size()==0) {return "";}
		StringBuffer buffer=new StringBuffer();
		List<Component> info = rp.getInfo();
		for (Component component : info) {
			if(webpa.containsKey(component.getJavaBeanName())) {
				buffer.append(" ").append(CacheMaps.AND).append(component.getDatabaseName()).append("=").append("#{").append(component.getDatabaseName()).append("} ");
			}
		}
		return buffer.toString();
	}
	/***
	 * 查询分页
	 * @param webpa
	 * @param rp
	 */
	public void   selectByPage(Map<String, Object> webpa, ReturnPage rp) {
		String tableName=getTableName(rp.getVal());
		StringBuffer buffer=new StringBuffer();
		StringBuffer pageBuff=new StringBuffer();
		buffer.append(CacheMaps.SELECT).append(getColumnsAS(rp)).append(CacheMaps.FROM).append(tableName).append(CacheMaps.WHERE);
		String sea= getSearch(webpa,rp);
		buffer.append(sea);
		//getLimit(rp);
		
		//分页
		pageBuff.append("SELECT COUNT(1) FROM ").append(tableName).append(CacheMaps.WHERE).append(sea);
		
		webpa.put("sql", pageBuff.toString());
		 Integer num = sqlSessionTemplate.selectOne("com.bxsbot.console.mapper.CommonMapper.selectByPageNum",webpa);
			if(null!=rp.getPage() && rp.getPage().getSortField()!=null){
				 buffer.append(" ORDER BY ").append(rp.getPage().getSortField()).append(" ").append(rp.getPage().getSortOrder());
			}else {
				 buffer.append(" ORDER BY id DESC ");
			}
		
		
			buffer.append(getLimit(rp,num,webpa));
		webpa.put("sql", buffer.toString());
		rp.getVal().put("list", sqlSessionTemplate.selectList("com.bxsbot.console.mapper.CommonMapper.selectByPage", webpa));
	}

	/*****
	 * 分页
	 * @param rp
	 * @param num
	 * @param webpa 
	 * @return
	 */
	private String getLimit(ReturnPage rp, Integer num, Map<String, Object> webpa) {
		//分页
		Integer currentPage=MapUtils.getValueInt(webpa, "currentPage");
		Integer pageSize=MapUtils.getValueInt(webpa, "pageSize");
		if(null==currentPage) {
			currentPage=1;
		}
		if(null==pageSize) {
			pageSize=10;
		}
		PageBean page=new PageBean(currentPage, pageSize, num);
		StringBuffer buffer=new StringBuffer();
		buffer.append(CacheMaps.LIMIT).append(" ").append((currentPage-1)*pageSize).append(",").append(pageSize);
		rp.getVal().put("page", page);
		return buffer.toString();
	}
	private String getSearch(Map<String, Object> webpa, ReturnPage rp) {
		if(webpa.size()==0) {return " AND sta=1 ";}
		StringBuffer buffer=new StringBuffer();
		//Integer sta=MapUtils.getValueInt(webpa, "sta");
		//if(null==sta) {
		//	buffer.append(" ").append(CacheMaps.AND).append(" sta=1 ");
		//}
		List<Component> search = rp.getSearch();
		for (Component component : search) {
			if(webpa.containsKey(component.getJavaBeanName())) {
				buffer.append(" ").append(CacheMaps.AND).append(component.getDatabaseName()).append("=").append("#{").append(component.getJavaBeanName()).append("} ");
			}
		}
		return buffer.toString();
	}
	/***
	 * 获取列名，带别名
	 * @param rp
	 * @return
	 */
	private String getColumnsAS(ReturnPage rp) {
		List<Component> info = rp.getInfo();
		StringBuffer buffer=new StringBuffer();
		for (Component component : info) {
			buffer.append(" ").append(component.getDatabaseName()).append(" AS ").append(component.getJavaBeanName()).append(",");
		}
		if(buffer.length()>0) {
			return buffer.substring(0,buffer.length()-1);
		}
		return null;
	}
	/***
	 * 获取列明
	 * @param webpa
	 * @param rp
	 * @return
	 */
	private String[] getColumnsInsert(Map<String, Object> webpa, ReturnPage rp) {
		List<Component> info = rp.getInfo();
		StringBuffer buffer=new StringBuffer();
		StringBuffer buffer2=new StringBuffer();
		String c1="";
		String c2="";
		for (Component component : info) {
			if(!"id".equals(component.getDatabaseName())) {
				buffer.append(" ").append(component.getDatabaseName()).append(",");
				buffer2.append(" #{").append(component.getJavaBeanName()).append("}").append(",");
			}
		}
		if(buffer.length()>0) {
			c1= buffer.substring(0,buffer.length()-1);
			c2=buffer2.substring(0,buffer2.length()-1);
		}
		String[] ss= new String[2];
		ss[0]=c1;
		ss[1]=c2;
		return ss;
	}
	/***
	 * 修改
	 * @param webpa
	 * @param rp
	 */
	public void update(Map<String, Object> webpa, ReturnPage rp) {
		StringBuffer buffer=new StringBuffer();
		Integer  id=MapUtils.getValueInt(webpa, "id");
		if(null!=id) {
			String tableName=getTableName(rp.getVal());
			buffer.append(CacheMaps.UPDATE).append(tableName).append(" SET ").append(getColumnsUpdate(webpa,rp)).append(CacheMaps.WHERE);	
			buffer.append(CacheMaps.AND).append("id = #{id}");
			webpa.put("sql", buffer.toString());
			 sqlSessionTemplate.update("com.bxsbot.console.mapper.CommonMapper.update", webpa);
		}
		
	}
	/***
	 * 获取列名 修改
	 * @param rp
	 */
	private String getColumnsUpdate(Map<String, Object> webpa,ReturnPage rp) {
		List<Component> info = rp.getInfo();
		StringBuffer buffer=new StringBuffer();
		for (Component component : info) {
			if(!"id".equals(component.getJavaBeanName())) {
				/*
				 * if(webpa.containsKey(component.getJavaBeanName())) {
				 * 
				 * }
				 */	
				buffer.append(" ").append(component.getDatabaseName()).append("=#{").append(component.getJavaBeanName()).append("}").append(",");
			}
		}
		if(buffer.length()>0) {
			return buffer.substring(0,buffer.length()-1);
		}
		return null;
	}
	/***
	 * 新增
	 * @param webpa
	 * @param rp
	 */
	public void save(Map<String, Object> webpa, ReturnPage rp) {
		StringBuffer buffer=new StringBuffer();
		String tableName=getTableName(rp.getVal());
		String[] cs=getColumnsInsert(webpa,rp);
		buffer.append(" INSERT INTO ").append(tableName).append(" ( ").append(cs[0]).append(")").append(" ").append(" VALUES(").append(cs[1]).append(")");
		webpa.put("sql", buffer.toString());
		  sqlSessionTemplate.update("com.bxsbot.console.mapper.CommonMapper.save", webpa);
	}
	/***
	 * 
	 * @param parameterMap
	 * @return
	 */
	private String getTableName(Map<String, Object> parameterMap) {
		if(parameterMap.containsKey("tableNamePar")) {
			return MapUtils.getValueStr(parameterMap, "tableNamePar");
		}
		return null;
	}
}
