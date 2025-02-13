package com.bxsbot.console.utils;
/***
 * 缓存
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bxsbot.console.bean.Button;
import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.Component;
import com.bxsbot.console.bean.CssJsFile;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.SysConfig;

public class CacheMaps {
	
	public final static String SELECT=" SELECT ";
	public final static String FROM=" FROM ";
	public final static String WHERE=" WHERE 1=1 ";
	public final static String AND=" AND ";
	public final static String LIMIT=" LIMIT ";
	public final static String UPDATE = " UPDATE ";
	/***
	 * page 封装config 通用select 只是info
	 */
	public static final String PAGE_CONFIG_KEY = "init_page_map";
	//所有
	public static final String PAGE_CONFIG_KEY_ALL = "init_page_map_all";
	
	public static Map<String, Cmds> cmdMap=new ConcurrentHashMap<String, Cmds>();
	/***
	 * id pages
	 */
	public static Map<Integer, Page> pages=new ConcurrentHashMap<Integer, Page>();
	/***
	 * 页面css，js,buttn
	 * 
	 */
	public static Map<Integer, List<CssJsFile>> pageJs=new ConcurrentHashMap<Integer, List<CssJsFile>>();
	
	public static Map<Integer, List<CssJsFile>> pageCss=new ConcurrentHashMap<Integer, List<CssJsFile>>();
	
	public static List<CssJsFile> pageJsCommon=new LinkedList<CssJsFile>();
	public static  List<CssJsFile> pageCssCommon=new  LinkedList<CssJsFile>();
	
	public static Map<Integer, List<Button>> pageButton=new ConcurrentHashMap<Integer, List<Button>>();
	public static List<Button> buttonCommon=new LinkedList<Button>();
	/***
	 * 组件
	 */
	public static Map<Integer, List<Component>> pageComponents=new ConcurrentHashMap<Integer, List<Component>>();
	public static List<Component> pageComponentsCommon=new LinkedList<Component>();
	
	/***
	 * 配置
	 */
	public static Map<String,Map<Integer,SysConfig>> sysConfig=new ConcurrentHashMap<String, Map<Integer,SysConfig>>();
}
