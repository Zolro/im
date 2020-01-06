package com.webim.im.config;

import com.webim.im.common.WebSession;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
  @Override
  public void modifyHandshake(
      ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    HttpSession httpSession = (HttpSession) request.getHttpSession();
    WebSession.saveUserInfo(request, sec);
    super.modifyHandshake(sec, request, response);
  }
}
