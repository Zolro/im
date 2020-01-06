package com.webim.im.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
///**
// * @Author zw
// * @Description 解决未登录用户连接websocket没办法连接 close问题
// * @Date 9:37 2020/1/3
// * @Param
// **/
//@WebListener
//public class RequestListener implements ServletRequestListener {
//
//    public void requestInitialized(ServletRequestEvent sre)  {
//        //将所有request请求都携带上httpSession
//        ((HttpServletRequest) sre.getServletRequest()).getSession();
//
//    }
//    public RequestListener() {
//        // TODO Auto-generated constructor stub
//    }
//
//    public void requestDestroyed(ServletRequestEvent arg0)  {
//        // TODO Auto-generated method stub
//    }
//}
