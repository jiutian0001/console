package com.bxsbot.console.utils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.ReturnPage;

public class PageUtils {
	/***
	 * 封装 search
	 * @param page
	 * @param parameterMap
	 * @return
	 */
	
	public static void  setSearch(Integer pageId,ReturnPage rp) {
		List<Component> search=new LinkedList<Component>();
		if(CacheMaps.pageComponents.containsKey(pageId)) {
			List<Component> list = CacheMaps.pageComponents.get(pageId);
			for (Component ct : list) {
				if(ct.getInSearchDisplay()==1) {
					search.add(ct);
				}
			}
		}
		if(CacheMaps.pageComponentsCommon.size()>0) {
			for (Component cct : CacheMaps.pageComponentsCommon) {
				if(cct.getInSearchDisplay()==1) {
					search.add(cct);
				}
			}
		}
		search = search.stream().sorted(Comparator.comparing(Component::getPosition))
	            .collect(Collectors.toList());
		rp.setSearch(search);
		
	}
	/***
	 * 封装 info
	 * @param page
	 * @param parameterMap
	 * @return
	 */
	public static void setInfo(Integer pageId,ReturnPage rp) {
	List<Component> info=new LinkedList<Component>();
	if(CacheMaps.pageComponents.containsKey(pageId)) {
		List<Component> list = CacheMaps.pageComponents.get(pageId);
		for (Component ct : list) {
			if(ct.getInInfoPage()==1) {
				if(Stringutils.isNotNull(ct.getConfig())) {
					if(CacheMaps.sysConfig.containsKey(ct.getConfig())) {
						rp.getVal().put(ct.getConfig(), CacheMaps.sysConfig.get(ct.getConfig()));
					}
				}
				info.add(ct);
			}
		}
	}
	if(CacheMaps.pageComponentsCommon.size()>0) {
		for (Component cct : CacheMaps.pageComponentsCommon) {
			if(cct.getInInfoPage()==1) {
				if(Stringutils.isNotNull(cct.getConfig())) {
					if(CacheMaps.sysConfig.containsKey(cct.getConfig())) {
						rp.getVal().put(cct.getConfig(), CacheMaps.sysConfig.get(cct.getConfig()));
					}
				}
				info.add(cct);
			}
		}
	}
	info = info.stream().sorted(Comparator.comparing(Component::getPosition))
            .collect(Collectors.toList());
	rp.setInfo(info);
	}
	/***
	 * 封装 table
	 * @param page
	 * @param parameterMap
	 * @return
	 */
	public static void setTable(Integer pageId,ReturnPage rp) {
	List<Component> table=new LinkedList<Component>();
	if(CacheMaps.pageComponents.containsKey(pageId)) {
		List<Component> list = CacheMaps.pageComponents.get(pageId);
		for (Component ct : list) {
			if(ct.getInTableDisplay()==1) {
				table.add(ct);
			}
		}
	}
	if(CacheMaps.pageComponentsCommon.size()>0) {
		for (Component cct : CacheMaps.pageComponentsCommon) {
			if(cct.getInTableDisplay()==1) {
				table.add(cct);
			}
		}
	}
	if(null!=table) {
		table = table.stream().sorted(Comparator.comparing(Component::getPosition))
	            .collect(Collectors.toList());
		rp.setTable(table);
	}
	
}
}
