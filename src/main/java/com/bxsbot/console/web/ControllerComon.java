package com.bxsbot.console.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.service.CommonService;
import com.bxsbot.console.service.DisService;
import com.bxsbot.console.utils.CacheMaps;
import com.bxsbot.console.utils.MapUtils;
import com.bxsbot.console.utils.Stringutils;
import com.bxsbot.console.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ControllerComon {

	@Autowired
	private DisService disService;
	
	@RequestMapping("/web/{cmd}")
	public String commonHtml(@PathVariable String cmd,HttpServletRequest request,Model model) {
		Map<String, Object> parameterMap = WebUtils.getParameterMap(request);
		
		if(Stringutils.isNotNull(cmd)) {
			if(CacheMaps.cmdMap.containsKey(cmd)) {
			ReturnPage rp= disService.html(parameterMap,CacheMaps.cmdMap.get(cmd));
			model.addAttribute("pageMap", parameterMap);
			model.addAttribute("returnPage", rp);
			if(null!=rp.getReturnPage()) {
				return rp.getReturnPage();
			}
			}
		}
		//默认页面
		return "index";
	}
}
