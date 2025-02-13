package com.bxsbot.console.utils;

import java.math.BigDecimal;
import java.util.Map;

public class MapUtils2 {
    
    /**
     * 获取Map中的字符串值
     * @param map Map对象
     * @param key 键名
     * @return 字符串值，如果为空则返回null
     */
    public static String getValueStr(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
    
    /**
     * 获取Map中的Integer值
     * @param map Map对象
     * @param key 键名
     * @return Integer值，如果为空或转换失败则返回null
     */
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
    
    /**
     * 获取Map中的Long值
     * @param map Map对象
     * @param key 键名
     * @return Long值，如果为空或转换失败则返回null
     */
    public static Long getValueLong(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 获取Map中的Double值
     * @param map Map对象
     * @param key 键名
     * @return Double值，如果为空或转换失败则返回null
     */
    public static Double getValueDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 获取Map中的BigDecimal值
     * @param map Map对象
     * @param key 键名
     * @return BigDecimal值，如果为空或转换失败则返回null
     */
    public static BigDecimal getValueBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 获取Map中的Boolean值
     * @param map Map对象
     * @param key 键名
     * @return Boolean值，如果为空则返回null
     */
    public static Boolean getValueBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.valueOf(String.valueOf(value));
    }
    
    /**
     * 安全地将值放入Map中
     * @param map Map对象
     * @param key 键名
     * @param value 值
     */
    public static void putValue(Map<String, Object> map, String key, Object value) {
        if (map != null && key != null) {
            map.put(key, value);
        }
    }
    
    /**
     * 检查Map中是否存在某个键的值（非null）
     * @param map Map对象
     * @param key 键名
     * @return 是否存在值
     */
    public static boolean hasValue(Map<String, Object> map, String key) {
        return map != null && key != null && map.get(key) != null;
    }
}