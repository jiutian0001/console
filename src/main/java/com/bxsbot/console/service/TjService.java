package com.bxsbot.console.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bxsbot.console.bean.Page;
import com.bxsbot.console.bean.PageBean;
import com.bxsbot.console.bean.ReturnPage;
import com.bxsbot.console.mapper.TjMapper;
import com.bxsbot.console.utils.DateUtils;
import com.bxsbot.console.utils.MapUtils;
import com.bxsbot.console.utils.PageUtils;
import com.bxsbot.console.utils.Stringutils;

@Service
public class TjService {
    @Autowired
    private TjMapper tj;

    public void selectByPage(Map<String, Object> webpa, ReturnPage rp) {
        Page page = rp.getPage();
        if (null != page) {
            // 设置查询参数
            setQueryParams(webpa);
            
            // 设置分页参数
            setPageParams(webpa, rp, page);
            
            // 获取数据并格式化
            List<Map<String, Object>> resultList = tj.selectByPage(webpa);
            formatData(resultList);
            rp.getVal().put("list", resultList);
            
            // 设置分页信息
            setPageInfo(webpa, rp);
        }
    }

    private void setQueryParams(Map<String, Object> webpa) {
        String startTime = MapUtils.getValueStr(webpa, "startTime");
        if (!Stringutils.isNotNull(startTime)) {
            startTime = DateUtils.getDate();
            webpa.put("startTime", startTime);
        }
        
        if (!Stringutils.isNotNull(MapUtils.getValueStr(webpa, "cc"))) {
            webpa.put("cc", "start_time");
        }
        if (!Stringutils.isNotNull(MapUtils.getValueStr(webpa, "cca"))) {
            webpa.put("cca", "DESC");
        }
        if (!Stringutils.isNotNull(MapUtils.getValueStr(webpa, "type"))) {
            webpa.put("type", "d");
        }
    }

    private void setPageParams(Map<String, Object> webpa, ReturnPage rp, Page page) {
        Integer pageId = page.getId();
        PageUtils.setSearch(pageId, rp);
        PageUtils.setTable(pageId, rp);
        PageUtils.setInfo(pageId, rp);
        
        Integer currentPage = MapUtils.getValueInt(webpa, "currentPage");
        Integer pageSize = MapUtils.getValueInt(webpa, "pageSize");
        currentPage = (currentPage == null) ? 1 : currentPage;
        pageSize = (pageSize == null) ? 10 : pageSize;
        
        webpa.put("st", (currentPage - 1) * pageSize);
        webpa.put("pageSize", pageSize);
        webpa.put("currentPage", currentPage);
    }

    private void setPageInfo(Map<String, Object> webpa, ReturnPage rp) {
        Integer currentPage = MapUtils.getValueInt(webpa, "currentPage");
        Integer pageSize = MapUtils.getValueInt(webpa, "pageSize");
        Integer num = tj.getTotalNum(webpa);
        PageBean pageNo = new PageBean(currentPage, pageSize, num);
        rp.getVal().put("page", pageNo);
    }

    private void formatData(List<Map<String, Object>> list) {
        for (Map<String, Object> row : list) {
            // 格式化价格数据
            formatPrice(row, "openPrice", 2);
            formatPrice(row, "closePrice", 4);
            formatPrice(row, "highPrice", 2);
            formatPrice(row, "lowPrice", 2);
            formatPrice(row, "resistanceLevel", 2);
            formatPrice(row, "supportLevel", 2);
            
            // 格式化成交量
            formatVolume(row, "buyVolume");
            formatVolume(row, "sellVolume");
            formatVolume(row, "totalVolume");
            formatVolume(row, "totalPrice");
            
            // 格式化比率
            formatPrice(row, "buySellRatio", 2);
            
            // 格式化百分比
            formatPercentage(row, "priceChangePercentage");
            formatPercentage(row, "vol1");
            formatPercentage(row, "vol2");
            formatPercentage(row, "vol3");
            formatPercentage(row, "pr1");
            formatPercentage(row, "pr2");
            formatPercentage(row, "pr3");
            //格式化日期 只要日
            formatDate(row,"startTime");
        }
    }

    private void formatPrice(Map<String, Object> row, String key, int scale) {
        Object value = row.get(key);
        if (value != null) {
            BigDecimal bd = new BigDecimal(value.toString());
            row.put(key, bd.setScale(scale, RoundingMode.HALF_UP).toString());
        }
    }

    private void formatVolume(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value != null) {
            double volume = Double.parseDouble(value.toString());
            String result;
            
            if (volume >= 100000000) {
                result = new BigDecimal(volume / 100000000)
                    .setScale(4, RoundingMode.HALF_UP).toString() + "亿";
            } else if (volume >= 1000000) {
                result = new BigDecimal(volume / 1000000)
                    .setScale(4, RoundingMode.HALF_UP).toString() + "M";
            } else {
                result = new BigDecimal(volume / 1000)
                    .setScale(4, RoundingMode.HALF_UP).toString() + "K";
            }
            
            row.put(key, result);
        }
    }

    private void formatPercentage(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value != null) {
            double percentage = Double.parseDouble(value.toString()) * 100;
            String result = new BigDecimal(percentage)
                .setScale(2, RoundingMode.HALF_UP).toString() + "%";
            row.put(key, result);
        }
    }
    private void formatDate(Map<String, Object> row, String key) {
    	 Object value = row.get(key);
    	 String[] dates=null;
    	 String[] days=null;
    	 String day=null;
    	 int index=0;
    	 if (value != null) {
    		 	dates=value.toString().split(" ");
    		 if(2==dates.length) {
    			 days=dates[0].split("-");
    			 row.put(key, days[2]);
    		 }else {
    			 day=dates[0];
    			 index=day.lastIndexOf("-");
    			 row.put(key,day.substring( index+1,index+3));
    		 }
    	 }
    	
    }
}