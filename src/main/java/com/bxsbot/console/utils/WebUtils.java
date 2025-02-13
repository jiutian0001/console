package com.bxsbot.console.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {
	/***
	 * 封装前台参数
	 * @param request
	 * @return
	 */
	public static  Map<String, Object> getParameterMap(HttpServletRequest request) {
		
		ConcurrentHashMap<String, Object> map=null;
		String val =null;
		  Map<String, String[]> parameterMap = request.getParameterMap();
		if(null!=parameterMap) {
			map=new ConcurrentHashMap<String, Object>();
			 String url = request.getRequestURI();
			 if(null!=url && url.length()>0) {
				 if(url.indexOf("?")>0) {
					 url=url.substring(0,url.indexOf("?"));
				 }
			 }
			//map.put("cmd", url);
			 for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
		            // 如果有多个值，取第一个值
		         if(null!=parameterMap.get(entry.getKey())) {
		        	val = entry.getValue()[0];
		        	 if(Stringutils.isNotNull(val)) {
		        		 map.put(entry.getKey(),val );
		        	 }
		        	
		         }  
				
		        }
		}
		return map;
	}
}
