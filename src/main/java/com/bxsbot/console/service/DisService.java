package com.bxsbot.console.service;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.bxsbot.console.bean.Button;
import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.utils.CacheMaps;
import com.bxsbot.console.utils.Stringutils;
/***
 * 调度
 */
@Service
public class DisService {
	@Autowired
	private ApplicationContext applicationContext;
	
	public ReturnPage html(Map<String, Object> webpa,Cmds cmd) {
		ReturnPage rp=new ReturnPage();
		if(CacheMaps.pages.containsKey(cmd.getPageId())) {
			Page page = CacheMaps.pages.get(cmd.getPageId());
			rp.getVal().put("tableNamePar", cmd.getTableName());
			rp.setPage(page);
			rp.setReturnPage(page.getReturnPage());
			try {
				String cmdClzz = cmd.getCmdClzz();
				if(!Stringutils.isNotNull(cmdClzz)) {
					cmdClzz="com.bxsbot.console.service.CommonService";
				}
				 Class<?> clazz = Class.forName(cmdClzz);
				 Object bean = applicationContext.getBean(clazz);
				  Method method = clazz.getMethod(cmd.getCmdFunc(),Map.class,rp.getClass());
			        // 调用方法并返回结果
				  method.invoke(bean, webpa,rp);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			//js css
			getWebAttr(page.getId(),rp);
			//获取select配置
		//	rp.getVal().put("sys_sta", CacheMaps.sysConfig.get("sys_sta"));
			
		}
		return rp;
	}
	private void getWebAttr(Integer  pageId, ReturnPage rp) {
		List<CssJsFile> css=new LinkedList<CssJsFile>();
		List<CssJsFile> js=new LinkedList<CssJsFile>();
		List<Button> button=new LinkedList<Button>();
		if(CacheMaps.pageCss.containsKey(pageId)) {
			css.addAll(CacheMaps.pageCss.get(pageId)) ;
		}
		if(null!=CacheMaps.pageCssCommon&& CacheMaps.pageCssCommon.size()>0) {
			css.addAll(CacheMaps.pageCssCommon);
		}
		if(CacheMaps.pageJs.containsKey(pageId)) {
			js.addAll(CacheMaps.pageJs.get(pageId)) ;
		}
		if(null!=CacheMaps.pageJsCommon&& CacheMaps.pageJsCommon.size()>0) {
			js.addAll(CacheMaps.pageJsCommon);
		}
		if(CacheMaps.pageButton.containsKey(pageId)) {
			button.addAll(CacheMaps.pageButton.get(pageId));
		}
		if(CacheMaps.buttonCommon!=null && CacheMaps.buttonCommon.size()>0) {
			//去掉重复的
			List<Button> tepButton=new LinkedList<Button>();
			boolean sta=true;
			if(button.size()>0) {
				for (Button b2 : CacheMaps.buttonCommon) {
					for (Button b1 : button) {
						if(b1.getLabel().equals(b2.getLabel())) {
							sta=false;
							break;
						}
					}
					if(sta) {
						button.add(b2);
					}
				}
				if(tepButton.size()>0) {
					button.addAll(tepButton);
				}
			}else {
				button.addAll(CacheMaps.buttonCommon);
			}
			
		}
		if(null!=css) {
			css=css.stream().sorted(Comparator.comparing(CssJsFile::getLoadOrder))
            .collect(Collectors.toList());
			rp.setCss(css);
		}
		
		if(null!=js) {
			js=js.stream().sorted(Comparator.comparing(CssJsFile::getLoadOrder))
            .collect(Collectors.toList());
			rp.setJs(js);
		}
		if(button!=null) {
			button=button.stream().sorted(Comparator.comparing(Button::getIdentifier))
            .collect(Collectors.toList());
			rp.setButtons(button);
		}
		
	}
}
