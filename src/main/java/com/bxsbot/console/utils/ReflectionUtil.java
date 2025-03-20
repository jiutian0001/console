package com.bxsbot.console.utils;

import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtil {

	  public static String invokeMethod(String classMethod, Map<String, Object> paramMap) throws Exception {
	        try {
	            // Split and validate input
	            String[] parts = classMethod.split("-");
	            if (parts.length != 2) {
	                throw new IllegalArgumentException("Input must be in format 'classPath-methodName'");
	            }
	            
	            String className = parts[0].trim();
	            String methodName = parts[1].trim();
	            
	            if (className.isEmpty() || methodName.isEmpty()) {
	                throw new IllegalArgumentException("classPath and methodName cannot be empty");
	            }
	            
	            // Load the class
	            Class<?> clazz = Class.forName(className);
	            
	            // Get the method with Map<String, String> parameter
	            Method method = clazz.getDeclaredMethod(methodName, Map.class);
	            
	            // Verify return type is String
	            if (!String.class.equals(method.getReturnType())) {
	                throw new IllegalStateException(
	                    "Method '" + methodName + "' must return String, but found: " + method.getReturnType().getName()
	                );
	            }
	            
	            // Make method accessible
	            method.setAccessible(true);
	            
	            // Create instance if method is not static
	            Object instance = null;
	            if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
	                try {
	                    instance = clazz.getDeclaredConstructor().newInstance();
	                } catch (Exception e) {
	                    throw new Exception("Failed to create instance of " + className + ": requires a no-args constructor", e);
	                }
	            }
	            
	            // Invoke method with the map parameter
	            Object result = method.invoke(instance, paramMap);
	            return (String) result;
	            
	        } catch (ClassNotFoundException e) {
	            throw new Exception("Class not found: " + classMethod, e);
	        } catch (NoSuchMethodException e) {
	            throw new Exception("Method not found or doesn't accept Map<String, String> in: " + classMethod, e);
	        } catch (Exception e) {
	            throw new Exception("Failed to invoke method for: " + classMethod, e);
	        }
	    }

}
