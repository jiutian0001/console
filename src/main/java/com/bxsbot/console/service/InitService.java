package com.bxsbot.console.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bxsbot.console.bean.Button;
import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.SysConfig;
import com.bxsbot.console.mapper.InitMapper;
import com.bxsbot.console.utils.CacheMaps;
import com.bxsbot.console.utils.Stringutils;

@Service
public class InitService {

	@Autowired
	private InitMapper init;
	public void init() {
		initCinfig();
		initCmds();
		initPage();
		initCssJs();
		initButtn();
		initComponents();
		initExchange();
	
	}

	

	private void initCinfig() {
		CacheMaps.sysConfig.clear();
		List<SysConfig>list=init.initConfig();
		Map<Integer,SysConfig> map;
		if(null!=list && list.size()>0) {
			for (SysConfig sc : list) {
				if(CacheMaps.sysConfig.containsKey(sc.getSysKey())) {
					CacheMaps.sysConfig.get(sc.getSysKey()).put(sc.getSysCode(), sc);
				}else {
					map=new ConcurrentHashMap<Integer, SysConfig>();
					map.put(sc.getSysCode(), sc);
					CacheMaps.sysConfig.put(sc.getSysKey(), map);
				}
			}
		}
	}

	private void initComponents() {
		CacheMaps.pageComponents.clear();
		CacheMaps.pageComponentsCommon.clear();
		List<Component> list=init.initComponent();
		List<Component> cs;
		if(null!=list) {
			for (Component c : list) {
				if(null!=c.getCp()&& c.getCp()==1) {
					CacheMaps.pageComponentsCommon.add(c);
				}else {
					if(CacheMaps.pageComponents.containsKey(c.getPageId())) {
						if(null!=c.getPageId()) {
							CacheMaps.pageComponents.get(c.getPageId()).add(c);
						}
					}else {
						if(null!=c.getPageId()) {
							cs=new LinkedList<Component>();
							cs.add(c);
							CacheMaps.pageComponents.put(c.getPageId(), cs);
						}
						
					}
				}
			}
		}
		
	}

	private void initButtn() {
		CacheMaps.pageButton.clear();
		CacheMaps.buttonCommon.clear();
		List<Button> list=init.initButtn();
		List<Button> bs=null;
		if(null!=list) {
			for (Button b : list) {
				if(null!=b.getIdentifier() && b.getIdentifier()==1) {
					CacheMaps.buttonCommon.add(b);
				}else {
					if(CacheMaps.pageButton.containsKey(b.getPageId())) {
						CacheMaps.pageButton.get(b.getPageId()).add(b);
					}else {
						bs=new LinkedList<Button>();
						bs.add(b);
						CacheMaps.pageButton.put(b.getPageId(), bs);
					}
				}
			}
		}
	}

	private void initCssJs() {
		CacheMaps.pageCss.clear();
		CacheMaps.pageJs.clear();
		CacheMaps.pageJsCommon.clear();
		CacheMaps.pageCssCommon.clear();
		List<CssJsFile> list=init.initCssJs();
		List<CssJsFile> js=null;
		List<CssJsFile> css=null;
		
		if(null!=list) {
			for (CssJsFile cj : list) {
				if(null!=cj.getTypeFiles()&& cj.getTypeFiles()==1) {
					if(cj.getIdentifier()==1) {
						//通用
						CacheMaps.pageCssCommon.add(cj);
					}else {
						//css
						if(CacheMaps.pageCss.containsKey(cj.getPageId())) {
							CacheMaps.pageCss.get(cj.getPageId()).add(cj);
						}else {
							css=new LinkedList<CssJsFile>();
							css.add(cj);
							CacheMaps.pageCss.put(cj.getPageId(), css);
						}
					}
					
				}else {
					//js
					if(null!= cj.getIdentifier() &&cj.getIdentifier()==1) {
						CacheMaps.pageJsCommon.add(cj);
					}else {
						if(CacheMaps.pageJs.containsKey(cj.getPageId())) {
							CacheMaps.pageJs.get(cj.getPageId()).add(cj);
						}else {
							js=new LinkedList<CssJsFile>();
							js.add(cj);
							CacheMaps.pageJs.put(cj.getPageId(), js);
						}
					}
					
				}
			}
		}
	}

	private void initPage() {
		CacheMaps.pages.clear();
		List<Page> list=init.initPage();
		SysConfig sys=null;
		Map<Integer,SysConfig> listPage=new ConcurrentHashMap<Integer, SysConfig>();
		Map<Integer,SysConfig> all=new ConcurrentHashMap<Integer, SysConfig>();
		String name=null;
		if(null!=list) {
			for (Page page : list) {
				name=Stringutils.getPYInitial(page.getName())+"-"+page.getName();
				CacheMaps.pages.put(page.getId(),page);
				//组装 通用配置  list页面
				if(null!=page.getType()&&"list".equals(page.getType())) {
					sys=new SysConfig(page.getId(),name);
					listPage.put(page.getId(), sys);
				}
				//页面Map
				all.put(page.getId(), new SysConfig(page.getId(),name));
			}
			if(listPage.size()>0) {
				CacheMaps.sysConfig.put(CacheMaps.PAGE_CONFIG_KEY, listPage);
			}
			if(all.size()>0) {
				CacheMaps.sysConfig.put(CacheMaps.PAGE_CONFIG_KEY_ALL, all);
			}
		}
		
	}

	private void initCmds() {
		CacheMaps.cmdMap.clear();
		List<Cmds> list=init.initCmd();
		if(null!=list) {
			for (Cmds cmds : list) {
				CacheMaps.cmdMap.put(cmds.getCmdName(), cmds);	
			}
		}
	}
	private void initExchange() {
		List<SysConfig>list=init.initExchange();
		Map<Integer, SysConfig> map=new ConcurrentHashMap<Integer, SysConfig>();
		if(null!=list) {
			for (SysConfig sysConfig : list) {
					sysConfig.setSysCode(sysConfig.getId());
				map.put(sysConfig.getSysCode(),sysConfig);
			}
			 CacheMaps.sysConfig.put("init_exchanges",map);
		}
		
	}
}
