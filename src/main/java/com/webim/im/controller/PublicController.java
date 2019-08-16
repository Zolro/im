package com.webim.im.controller;

import com.webim.im.Server.UserServer;
import com.webim.im.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
public class PublicController {
    @Autowired
    UserServer userServer;
    @PostMapping("/public/login")
    public Object login(String username,String password, HttpSession session){
        User user = userServer.findByTopic(username);
        if(user==null){
            return "redirect:/login.html";
        }
        if(!user.getPassword().equals(password)){
            return "redirect:/login.html";
        }
        session.setAttribute("UserInfo",user);
        return "redirect:/index4.html?UserInfo="+user.getId();
    }
    @PostMapping("/public/register")
    public Object createUser(String username,String password, String sign, MultipartFile avatar) {
        userServer.createUser(username,password, sign, avatar);
        return "redirect:/login.html";
    }
}
