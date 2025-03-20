package com.bxsbot.console.web;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bxsbot.console.bean.Menu;
import com.bxsbot.console.bean.SysConfig;
import com.bxsbot.console.service.IndexDataService;
import com.bxsbot.console.service.InitService;
import com.bxsbot.console.service.MenuService;
import com.bxsbot.console.utils.CacheMaps;

@Controller
public class HomeController {

	@Autowired
	private InitService initService;
	@Autowired
	private IndexDataService indexDataService;
	/*
	 * @GetMapping("/") public String index(Model model) {
	 * 
	 * 
	 * model.addAttribute("name", "World"); return "home"; // 返回 index.html 模板 }
	 */
    @Autowired
    private MenuService menuService;
    @GetMapping("/")
    public String home(Model model) {
        List<Menu> menuTree = menuService.getMenuTree();
        model.addAttribute("menus", menuTree);
        return "home"; // 返回 home.html 视图
    }
    
    @GetMapping("/sys/refresh")
    public String Refresh() {
    	initService.init();
    	return "redirect:/";
	}
    
    /***
     * 去上传页面
     * @return
     */
    @GetMapping("/sys/up")
    public String home() {
    	return "upload";
	} 
    
    
    @GetMapping("/index/data")
    public String indexData(Model model) {
    	Map<Integer, SysConfig> web_nav_type = CacheMaps.sysConfig.get("web_nav_type").entrySet().stream()
    			   .sorted(Comparator.comparing(e -> Optional.ofNullable(e.getValue().getSysVal()).orElse("999999")))
    			   .collect(Collectors.toMap(
    			       Map.Entry::getKey,
    			       Map.Entry::getValue,
    			       (oldVal, newVal) -> oldVal,
    			       LinkedHashMap::new
    			   ));
    		//网站导航
    	  model.addAttribute("web_nav_type",web_nav_type);
    	//工作计划
    	  Map<Integer, SysConfig> msgEvent = CacheMaps.sysConfig.get("msgEvent").entrySet().stream()
   			   .sorted(Comparator.comparing(e -> Optional.ofNullable(e.getValue().getSysVal()).orElse("999999")))
   			   .collect(Collectors.toMap(
   			       Map.Entry::getKey,
   			       Map.Entry::getValue,
   			       (oldVal, newVal) -> oldVal,
   			       LinkedHashMap::new
   			   ));
    	  model.addAttribute("msgEvent", msgEvent);
        return "index/index"; // 
    }
    
    
    
    @GetMapping("/get/index/data")
    @ResponseBody
    public List ajaxHome(Integer type,Integer code) {
    	if(1==type) {
    		//查询工作计划
    		return indexDataService.getMsg(code);
    	}else if (2==type) {
			//查询二级数据
    		return indexDataService.getData(code);
		}else if (3==type) {
			//查询web导航
			return indexDataService.getWebNav(code);
		}
		return null;
	}
}
