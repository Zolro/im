package com.webim.im.Interceptor;

import com.webim.im.common.WebSession;
import com.webim.im.common.pojo.UserInfoPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends WebSession implements HandlerInterceptor {
  private static final Logger logger = LogManager.getLogger(LoginInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    UserInfoPojo pojo = getLoginUserInfo(request.getSession());
    /** 不等于null 登录成功 */
    if (pojo != null) {
      logger.debug("登录成功");

      return true;
    }
    // 说明用户未登陆
    request.setAttribute("msg", "没有相应权限请先登陆");
    logger.debug("没有登录");
    return false;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {}
}
