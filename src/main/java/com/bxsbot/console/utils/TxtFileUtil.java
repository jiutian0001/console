package com.bxsbot.console.utils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TxtFileUtil {

    /**
     * 读取文本文件的第一行
     * @param filePath 文件路径
     * @return 文件第一行内容，若文件不存在或为空则返回 null
     * @throws IOException 如果读取文件失败
     */
    public static String readFirstLine(Map<String,Object> map) throws IOException {
    	String filePath=MapUtils.getValueStr(map, "path");
    	String fileName=MapUtils.getValueStr(map, "fileName");
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File file = new File(filePath.trim()+"/"+fileName.trim());
        
        // 如果文件不存在，直接返回 null
        if (!file.exists()) {
            return null;
        }
        
        // 使用 try-with-resources 确保流被关闭
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
            String firstLine = reader.readLine();
            return firstLine != null ? firstLine.trim() : null;
        } catch (IOException e) {
            throw new IOException("读取文件失败: " + filePath, e);
        }
        // 无需手动关闭流，try-with-resources 会自动处理
    }

    /**
     * 将内容写入文本文件，如果文件不存在则创建
     * @param filePath 文件路径
     * @param content 要写入的内容（可以为 null 或空）
     * @throws IOException 如果创建或写入文件失败
     */
    public static String writeContent(Map<String,Object> map) throws IOException {
    	
    	String filePath=MapUtils.getValueStr(map, "path");
    	String fileName=MapUtils.getValueStr(map, "fileName");
    	String content=MapUtils.getValueStr(map, "data");
    	
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File file = new File(filePath.trim()+"/"+fileName.trim());
        
        // 如果父目录不存在，创建父目录
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("创建父目录失败: " + filePath);
            }
        }
        
        // 使用 try-with-resources 确保流被关闭
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8))) {
            if (content != null && !content.trim().isEmpty()) {
                writer.write(content);
                writer.newLine(); // 添加换行符保持一致性
            }
            // 流会在此处自动关闭
        } catch (IOException e) {
            throw new IOException("写入文件失败: " + filePath, e);
        }
        return "1";
    }

}