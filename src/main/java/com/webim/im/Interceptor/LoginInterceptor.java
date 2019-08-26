package com.webim.im.Interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getSession(  ));
        HttpSession session = request.getSession();
        Object _tb_token_ =  session.getAttribute("_tb_token_");
        Object _userid_ =  session.getAttribute("__userid__");
        // 不等于null 登录成功
        if(_tb_token_!=null&&_userid_!=null){
            System.out.println("登录成功");
            return true;
        }
        //说明用户未登陆
        request.setAttribute("msg","没有相应权限请先登陆");
        System.out.println("登录失败");
        return false;
    }




    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
