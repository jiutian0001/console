package com.bxsbot.console.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {
	/*
	 * public static <T> T getValue(Map<String, Object> map, String key, Class<T>
	 * type) { if(key!=null && key.length()>0 && null!=map && map.containsKey(key))
	 * { Object value = map.get(key); if (value != null && type.isInstance(value)) {
	 * return type.cast(value); } } return null; // 或者抛出异常，根据需要 }
	 */
	public static String getValueStr(Map<String, Object> map, String key) {
		 if(key!=null &&  key.length()>0 && null!=map && map.containsKey(key)) {
			 Object value = map.get(key);
		        if (value != null ) {
		            return  value.toString();
		        } 
		 }
		return null;
	}
	 public static Integer getValueInt(Map<String, Object> map, String key) {
		 Object value = map.get(key);
	        if (value == null) {
	            return null;
	        }
	        if (value instanceof Integer) {
	            return (Integer) value;
	        }
	        try {
	            return Integer.parseInt(String.valueOf(value));
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	 // 将单个 Map 转换为 JavaBean
	    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
	        try {
	            T bean = beanClass.getDeclaredConstructor().newInstance();
	            for (Map.Entry<String, Object> entry : map.entrySet()) {
	                String propertyName = entry.getKey();
	                Object propertyValue = entry.getValue();

	                Field field;
	                try {
	                    field = beanClass.getDeclaredField(propertyName);
	                } catch (NoSuchFieldException e) {
	                    continue; // 如果字段不存在，则跳过该属性
	                }

	                field.setAccessible(true);
	                field.set(bean, propertyValue);
	            }
	            return bean;
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to convert map to bean", e);
	        }
	    }

	    // 将 List<Map<String, Object>> 转换为 List<JavaBean>
	    public static <T> List<T> mapListToBeanList(List<Map<String, Object>> mapList, Class<T> beanClass) {
	        List<T> beanList = new ArrayList<>();
	        for (Map<String, Object> map : mapList) {
	            T bean = mapToBean(map, beanClass);
	            beanList.add(bean);
	        }
	        return beanList;
	    }
	
}
