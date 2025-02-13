package com.bxsbot.console.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	
	/***
	 * 获取日期文件夹
	 * @return
	 */
	public static String getCurrentDateFolder() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        
        // 定义日期格式，格式为 "yyyyMMdd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        
        // 格式化当前日期
        String formattedDate = currentDate.format(formatter);
        
        // 返回日期作为文件夹名
        return formattedDate;
    }
	/***
	 * 获取日期
	 * @return
	 */
	public static String getDate() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        
        // 定义日期格式，格式为 "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 格式化当前日期
        String formattedDate = currentDate.format(formatter);
        
        // 返回日期作为文件夹名
        return formattedDate;
    }
}
