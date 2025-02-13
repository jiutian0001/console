package com.bxsbot.console.web;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bxsbot.console.utils.DateUtils;
import com.bxsbot.console.utils.OCRUtils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    // 设置文件上传目录
    private final String uploadDir = "C:\\images\\";

    @PostMapping("/file")
    public Response uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new Response("File is empty", null, false,null);
        }

        String fileName = UUID.randomUUID().toString()+".png";
        String dateFolder=DateUtils.getCurrentDateFolder();
        try {
        	String imgpath= dateFolder+File.separator+fileName;
        	String returnPath = DateUtils.getCurrentDateFolder()+"/"+fileName;
            File targetFile = new File(uploadDir + imgpath);
            if(!targetFile.exists()) {
            	targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            //解析图片文字
            
            String work = OCRUtils.extractTextFromImage(targetFile.toString());
            // 返回文件的访问路径
            return new Response("File uploaded successfully", returnPath, true,work);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("File upload failed", null, false,null);
        }
    }

    // 响应对象
    public static class Response {
        private String message;
        private String filePath;
        private String works;
        private boolean success;

        public Response(String message, String filePath, boolean success, String works) {
            this.message = message;
            this.filePath = filePath;
            this.success = success;
            this.works=works;
        }

        // getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

		public String getWorks() {
			return works;
		}

		public void setWorks(String works) {
			this.works = works;
		}
        
    }
    /***
     * 去上传页面
     * @return
     */
    @GetMapping("/")
    public String home() {
    	return "upload";
	} 
}
