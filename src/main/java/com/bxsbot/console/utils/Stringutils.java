package com.bxsbot.console.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

public class Stringutils {

	public static boolean isNotNull(String str) {
		if(null!=str && str.length()>0) {
			return true;
		}
		return false;
	}

	public static String getPYInitial(String str){
		StringBuffer buffer=new StringBuffer(1);
		if(isNotNull(str)) {
			if (str.matches("[a-zA-Z]+")) {
	            // 输入是拼音，直接获取首字母大写
				buffer.append(str.substring(0, 1).toUpperCase());
	        } else {
	            // 输入是汉字，获取第一个汉字的拼音首字母
	            char firstChar = str.charAt(0);
	            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar);

	            if (pinyinArray != null) {
	                // 获取拼音的首字母并转换为大写
	                char firstLetter = pinyinArray[0].charAt(0);
	                buffer.append(Character.toUpperCase(firstLetter));
	            } else {
					/*
					 * for (int i = 0; i < str.length(); i++) { char character = str.charAt(i);
					 * String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(character); if
					 * (pinyin != null) { // 获取拼音的首字母并大写 char firstLetter = pinyin[0].charAt(0);
					 * 
					 * // 如果是第一个字母，则大写 if (buffer.length() == 0) {
					 * buffer.append(Character.toUpperCase(firstLetter)); } else { // 后续字母小写
					 * buffer.append(Character.toLowerCase(firstLetter)); } } }
					 */
	            }
	        }
	    }
		return buffer.toString();
	}
		
	public static void main(String[] args) {
		System.out.println(getPYInitial("css列表"));
	}
}
