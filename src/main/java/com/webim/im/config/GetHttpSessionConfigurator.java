package com.webim.im.config;

import com.webim.im.common.WebSession;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
  @Override
  public void modifyHandshake(
      ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    WebSession.saveUserInfo(request, sec);
    super.modifyHandshake(sec, request, response);
  }
}
