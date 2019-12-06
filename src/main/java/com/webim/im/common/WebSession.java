package com.webim.im.common;

import com.webim.im.common.pojo.UserInfoPojo;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.server.HandshakeRequest;
import java.util.Map;

public abstract class WebSession {
  public static final String USERIDKEY = "__userid__";
  public static final String TOKENKEY = "_tb_token_";
  public static final String SESSIONKEY = "HTTPSESSION_IM_KEY";

  public UserInfoPojo getLoginUserInfo(EndpointConfig config) throws Exception {
    Map<String, Object> map = config.getUserProperties();
    if (map == null) {
      throw new Exception("获取 Session 失败");
    }
    HttpSession session1 = (HttpSession) map.get(SESSIONKEY);
    if (session1 == null) {
      return null;
    }
    UserInfoPojo pojo = new UserInfoPojo();
    pojo.setUserId(Integer.valueOf(String.valueOf(session1.getAttribute(USERIDKEY))));
    pojo.setToken(String.valueOf(session1.getAttribute(TOKENKEY)));
    return pojo;
  }

  public UserInfoPojo getLoginUserInfo(HttpSession session) {
    UserInfoPojo pojo = new UserInfoPojo();
    String id=String.valueOf(session.getAttribute(USERIDKEY));
    if ("null".equals(id)) {
      return null;
    }
    pojo.setUserId(Integer.valueOf(id));
    pojo.setToken(String.valueOf(session.getAttribute(TOKENKEY)));
    return pojo;
  }

  public static void saveUserInfo(HandshakeRequest request, EndpointConfig config) {
    HttpSession httpSession = (HttpSession) request.getHttpSession();
    if (httpSession != null) {
      config.getUserProperties().put(SESSIONKEY, httpSession);
    }
  }
}
