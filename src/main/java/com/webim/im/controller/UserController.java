package com.webim.im.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Server.UserServer;
import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.Record;
import com.webim.im.entity.User;
import com.webim.im.utils.ImageUploadUtils;
import com.webim.im.utils.Result;
import com.webim.im.view.ImageResultView;
import com.webim.im.view.ImageView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    UserServer userServer;

    @GetMapping("/user/{userid}/init.json")
    public Object init(@PathVariable Integer userid) {
//        User info = (User) session.getAttribute("UserInfo");
//        if(info==null){
//            return Result.of(0, "未登录");
//        }  // TODU  需要判断是否登录才能获取加载数据
        return userServer.init(userid);
    }

    @PostMapping("/upload/image")
    @ResponseBody
    public Object uploadFile(HttpSession session, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        User info = (User) session.getAttribute("UserInfo");
        if (info == null) {
            return Result.of(0, "未登录");
        }
        //文件上传到ftp服务器
        String name = multipartFile.getOriginalFilename();
        ImageView iv = ImageUploadUtils.upload(name, multipartFile);
        ImageResultView irv = new ImageResultView();
        if (iv.getState() == "SUCCESS") {
            irv.setCode(0);
            ImageResultView.ImageData ss = new ImageResultView().new ImageData();
            ss.setSrc("http://shoestp.oss-us-west-1.aliyuncs.com" + iv.getUrl());
            irv.setData(ss);
        } else {
            irv.setCode(-1);
        }
        return irv;
    }
    @GetMapping("/userlist/{userid}")
    public Object test(@PathVariable Integer userid){
        return userServer.getuserlist(userid);
    }

    // 好友申请列表
    @GetMapping("/getfriendlist/{userid}")
    public List<ApplyUserListView>  getfriendlist(@PathVariable  Integer userid){
        return  userServer.getapplyfriendlist(userid);
    }
    /**
     * @Author zw
     * @Description 修改好友申请列表状态
     * @Date 11:07 2019/8/9
     * @Param
     **/
    @PostMapping("/updapplyfriends")
    public Object updapplyfriends(Integer applyuserid,Integer state,String reply,Integer groupid){
        userServer.updApplyUser(applyuserid,state,reply,groupid);
        return Result.of(200,"申请通过");
    }

    /**
     * @Author zw
     * @Description 获取当前聊天对象的未读取的消息
     * @Date 20:40 2019/8/13
     * @Param
     **/
    @GetMapping("/findUserRead")
    public  List<Record>  findUserRead(Integer formuserid, Integer touserid){
        return   userServer.findUserRead(formuserid,touserid);
    }
}
