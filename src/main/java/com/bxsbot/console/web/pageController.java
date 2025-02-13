package com.bxsbot.console.web;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bxsbot.console.bean.Cmds;
import com.bxsbot.console.bean.ComponentForm;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.service.DisService;
import com.bxsbot.console.service.PageService;
import com.bxsbot.console.utils.CacheMaps;
import com.bxsbot.console.utils.Stringutils;
import com.bxsbot.console.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class pageController {

	@Autowired
	private PageService  pageService;
	@Autowired
	private DisService disService;

	
	@PostMapping("/page/save")
	public String save(@ModelAttribute ComponentForm componentForm,Page page,Cmds cmds) {
		pageService.save(componentForm,page,cmds);
		return "redirect:/web/page_list";
	}
	@GetMapping("/page/add")
	public String add(HttpServletRequest request,Model model) {
	Map<String, Object> parameterMap = WebUtils.getParameterMap(request);
		String cmd="page_info";
			if(CacheMaps.cmdMap.containsKey(cmd)) {
			ReturnPage rp= disService.html(parameterMap,CacheMaps.cmdMap.get(cmd));
			model.addAttribute("pageMap", parameterMap);
			model.addAttribute("returnPage", rp);
			}
		
		return "page/info_page";
	}
}
