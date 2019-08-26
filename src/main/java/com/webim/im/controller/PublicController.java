package com.webim.im.controller;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Server.UserServer;
import com.webim.im.pub.upload.ImageUpload;
import com.webim.im.utils.ImageUploadUtils;
import com.webim.im.utils.Result;
import com.webim.im.view.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
@RestController
public class PublicController {
    @Autowired
    UserServer userServer;
    @Autowired
    ObjectMapper objectMapper;
    @PostMapping("/user/upload")
    public  String upload( MultipartFile file){
        try {
            ImageView view = ImageUploadUtils.upload(file.getOriginalFilename(), file);
            return   objectMapper.writeValueAsString(Result.of(200,"上传成功",objectMapper.writeValueAsString(view))) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return   objectMapper.writeValueAsString(Result.of(0,"失败")) ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * @Author zw
     * @Description IM前端向后端获取用户ID  如果对应的用户ID不存在则创建用户
     * @Date 10:38 2019/8/21
     * @Param
     **/
    @GetMapping("/getImUserInfo")
    public Integer getImUserInfo( HttpSession session){
        Object _userid_= session.getAttribute("__userid__");
        Object _tb_token_= session.getAttribute("_tb_token_");
        Map ob=null;
        if(_userid_!=null&&_tb_token_!=null){
            ob=getuserinfo(String.valueOf(_userid_),String.valueOf(_tb_token_));
            Map<String,Map<String,Object>> map= ob;
            String name=String.valueOf(map.get("result").get("nickname"));
            Integer topic=Integer.valueOf(String.valueOf(map.get("result").get("id")));
            Integer imuserid= userServer.getImUserInfo(topic,name);
            System.out.println("imuserid:"+imuserid);
        return  imuserid ;
        }
        return  null;
    }
    @Value("${sso.domain}")
    private String ssoDomain;
    @Autowired
    RestTemplate restTemplate;
    private Map getuserinfo(String userid, String _tb_token_){
        String url= ssoDomain+"/member/"+userid+"?&_tb_token_="+_tb_token_;
        return  restTemplate.getForObject(url,Map.class);
    }

}
