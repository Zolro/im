package com.webim.im.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xinlianshiye.clouds.sso.common.interceptor.AbstractSpringLoginInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
@Component
public class SpringLoginInterceptor extends AbstractSpringLoginInterceptor {

    @Value(value = "${sso.domain}")
    private  String ssoDomain;
    @Override
    public String getSSoDomain() {
        return ssoDomain;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
