package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomerException;
import com.example.server.Fileserver;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private Fileserver fileserver;

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName,HttpServletResponse response) throws Exception {
        String filePath = System.getProperty("user.dir")+"/files/";//获取当前项目根路径
        String realPath = filePath + fileName;//最终目录
        boolean exist = FileUtil.exist(realPath);
        if(!exist){
            throw  new CustomerException("no file");
        }
        //读取文件字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os =response.getOutputStream();
        //输出流对象写到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        String filePath = System.getProperty("user.dir")+"/files/";
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String filename =System.currentTimeMillis() +"_"+ file.getOriginalFilename();
        //String encodeFileName = URLEncoder.encode(Filename, StandardCharsets.UTF_8);
        //FileUtil.writeBytes(bytes,filePath+encodeFileName);
        FileUtil.writeBytes(bytes,filePath+filename);
        String url="http://localhost:8081/files/download/"+filename;
        return Result.success(url);
    }
}
