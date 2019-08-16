package com.webim.im.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.entity.User;
import com.webim.im.pub.upload.ImageUpload;
import com.webim.im.view.ImageView;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ImageUploadUtils {
    public static ImageView upload(String name, MultipartFile file) throws  Exception{
        ImageView view = ImageUpload.upload(User.class, name, MultipartFileToFile(file));
        return  view;
    };
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用当前时间作为文件名，防止生成的临时文件重复
        try {
            File file = File.createTempFile(System.currentTimeMillis() + "", prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
