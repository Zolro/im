package com.webim.im.controller;

import com.webim.im.Server.UserServer;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.Group;
import com.webim.im.entity.User;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    UserServer userServer;
    @Autowired
    RedisReceiver redisReceiver;
    @GetMapping("/user/home")
    public Object home(ModelAndView mav, HttpSession session){
        User info = (User) session.getAttribute("UserInfo");
        if(info==null){
            return "redirect:/login.html";
        }
        mav.setViewName("home");
        return mav;
    }
//    // 修改个人签名
//    @ResponseBody
//    @PostMapping("/user/update/sign")
//    public Object updateSign(HttpSession session,String sign){
//        User info = (User) session.getAttribute("UserInfo");
//        if(info==null){
//            return Result.of(0, "未登录");
//        }
//        userServer.updsign(info.getId(),sign);
//        return Result.of(200,"签名修改成功");
//    }

//    /**
//     * @Author zw
//     * @Description 添加好友  到申请好友列表中
//     * @Date 14:45 2019/8/7
//     * @Param  touserId 接受者ID，
//     *         发送者给好友的分组，
//     *         添加类型 type 群还是好友 ，
//     *         postscript  附言
//     **/
//    @ResponseBody
//    @PostMapping("/user/friend/add")
//    public Object addFriend(Integer  formId,Integer touserId,Integer groupiden,Integer type,String postscript){
//        if(!redisReceiver.isUserOnline(formId)){
//            return Result.of(0, "未登录");
//        }
//        return userServer.getfriedns( formId, touserId, groupiden, type, postscript);
//
//    }

}
