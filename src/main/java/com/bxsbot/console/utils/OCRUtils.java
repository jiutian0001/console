package com.bxsbot.console.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRUtils {
    // 设置Tesseract的语言路径
   // private static final String TESSDATA_PATH = "C:\\Users\\Administrator\\eclipse-workspace\\console\\src\\main\\resources\\tessdata"; // tessdata路径
    private static final String LANGUAGE = "eng"; // 默认是英文，可以根据需要设置
    public static final String DEFAULT_TESSERACT_PATH = "C:/Program Files/Tesseract-OCR/tessdata";
    public static String extractTextFromImage(String imagePath) {
        ITesseract instance = new Tesseract();
        
        // 设置Tesseract的语言路径
     //   instance.setTessVariable("tessdata", TESSDATA_PATH);
        instance.setLanguage(LANGUAGE);
        instance.setDatapath(DEFAULT_TESSERACT_PATH);
        try {
            // 读取图片文件
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            
            // 使用Tesseract解析图片中的文字
            String result = instance.doOCR(bufferedImage);
            
            // 输出结果（去除多余的空格和换行）
            return processResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

 // 处理OCR结果，去除多余空格或换行，返回只包含字母的词语，且以逗号分隔
    private static String processResult(String result) {
    	if(!Stringutils.isNotNull(result)) {
    		return null;
    	}
    	  result = result.replaceAll("ii", " ");  // 根据实际需要调整
        // 删除多余的换行符和空格
        String processedResult = result.replaceAll("\n", " ").replaceAll("\\s+", " ").trim();
      
        
        // 使用正则表达式筛选出只包含英文字母的词语
        String[] words = processedResult.split(" ");
        
        // 定义一个正则表达式来匹配只包含英文字母的词语
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        
        // 创建一个StringBuilder来存储筛选后的词语
        StringBuilder filteredResult = new StringBuilder();
        
        for (String word : words) {
        	
            Matcher matcher = pattern.matcher(word);
            if (matcher.matches()) {
                // 如果匹配，则添加到结果字符串中
                if (filteredResult.length() > 0) {
                    filteredResult.append(",");  // 词语之间以逗号分隔
                }
                filteredResult.append(word);
            }
        }

        // 返回以逗号分隔的词语
        return filteredResult.toString();
    }

    public static void main(String[] args) {
        // 示例：调用工具类，传入图片路径并获取解析的文字
        String imagePath = "C:\\Users\\Administrator\\Desktop\\123.png"; // 图片文件路径
        String resultText = extractTextFromImage(imagePath);
        System.out.println("Extracted Text: " + resultText);
    }
}

