package com.webim.im.controller.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Server.UserServer;
import com.webim.im.common.WebSession;
import com.webim.im.config.GetHttpSessionConfigurator;
import com.webim.im.controller.ConvertMessageMethod;
import com.webim.im.entity.User;
import com.webim.im.model.Enum.cmdEnum;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.webServer.WebUserServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@EnableScheduling
@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer extends WebSession { // 每个人会分配一个独立的实例
  private static final Logger logger = LogManager.getLogger(WebSocketServer.class);
  static WebUserServer webUserServer;

  @Autowired
  void setWebUserServer(WebUserServer webUserServer) {
    WebSocketServer.webUserServer = webUserServer;
  }

  static ObjectMapper objectMapper;

  @Autowired
  void setObjectMapper(ObjectMapper objectMapper) {
    WebSocketServer.objectMapper = objectMapper;
  }

  static RedisReceiver redisReceiver;

  @Autowired
  void setRedisReceiver(RedisReceiver redisReceiver) {
    WebSocketServer.redisReceiver = redisReceiver;
  }

  static ConvertMessageMethod convertMessageMethod;

  @Autowired
  void setConvertMessageMethod(ConvertMessageMethod convertMessageMethod) {
    WebSocketServer.convertMessageMethod = convertMessageMethod;
  }

  static UserServer userServer;

  @Autowired
  void setUserServer(UserServer userServer) {
    WebSocketServer.userServer = userServer;
  }

  /** 连接建立成功调用的方法 */
  @OnOpen
  public void onOpen(Session session, EndpointConfig config) throws Exception {
    logger.debug("建立成功");
    User user = userServer.findByTopic(getLoginUserInfo(config).getUserId());
    if (user == null) {
      throw new Exception("查询对象不存在");
    }
    redisReceiver.onOpen(session, user.getId(), getLoginUserInfo(config).getToken());
    // 记载用户初始化信息 连接后台后 发送给前端
    redisReceiver.sendToUserid(user.getId(), webUserServer.init(user.getId()));
  }

  /** 连接关闭调用的方法 */
  @OnClose
  public void onClose(Session session) {
    redisReceiver.onClose(session);
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    try {
      MessageBody map = objectMapper.readValue(message, MessageBody.class);
      if (map.getMsgtype() == msgtypeEnum.request.ordinal()) {
        /** 聊天记录 */
        if (map.getCmd() == cmdEnum.chat.ordinal()) {
          redisReceiver.onMessage(map, session);
        }
        /** 获取用户列表 查询用户列表 */
        if (map.getCmd() == cmdEnum.httprequest.ordinal()) {
          if (map.getUrl().equals("init")) {
            redisReceiver.receiveMessage(convertMessageMethod.init(map));
          }
          /** 测试用来 查看所有用户的 无用方法 */
          if (map.getUrl().equals("getuserlist")) {
            redisReceiver.receiveMessage(convertMessageMethod.getuserlist(map));
          }
          /** 获取好友申请列表 */
          if (map.getUrl().equals("getapplyfriendlist")) {
            redisReceiver.receiveMessage(convertMessageMethod.getapplyfriendlist(map));
          }
          /** 添加到好友申请表 */
          if (map.getUrl().equals("getfriedns")) {
            redisReceiver.receiveMessage(convertMessageMethod.getfriedns(map));
          }
          /** 修改好友申请表 添加到好友表中 */
          if (map.getUrl().equals("updApplyUser")) {
            redisReceiver.receiveMessage(convertMessageMethod.updApplyUser(map));
          }
          /** 获取当前聊天对象的未读取的消息 并修改成已读状态 */
          if (map.getUrl().equals("findUserRead")) {
            redisReceiver.receiveMessage(convertMessageMethod.findUserRead(map));
          }
          /** 根据用户id 显示用户好友聊天列表 */
          if (map.getUrl().equals("findUseridRecordCustom")) {
            redisReceiver.receiveMessage(convertMessageMethod.findUseridRecordCustom(map));
          }
          /** 根据用户id 修改个性签名 */
          if (map.getUrl().equals("updsign")) {
            redisReceiver.receiveMessage(convertMessageMethod.updsign(map));
          }
          /** 单击 用户Id 去SSO ID 获取用户信息 以及和对应的聊天记录 */
          if (map.getUrl().equals("getSSOIdUserAndRecord")) {
            redisReceiver.receiveMessage(convertMessageMethod.getSSOIdUserAndRecord(map));
          }
          /** 查询根据名称模糊查询消息列表 */
          if (map.getUrl().equals("getlistUserName")) {
            redisReceiver.receiveMessage(convertMessageMethod.getlistUserName(map));
          }
          /** 查询根据名称模糊查询好友列表 */
          if (map.getUrl().equals("getlistUserNamefriend")) {
            redisReceiver.receiveMessage(convertMessageMethod.getlistUserNamefriend(map));
          }
          /** 删除好友 */
          if (map.getUrl().equals("delfriendAndRecord")) {
            redisReceiver.receiveMessage(convertMessageMethod.delfriendAndRecord(map));
          }
          /** 好友聊天记录 */
          if (map.getUrl().equals("UserRecordPage")) {
            redisReceiver.receiveMessage(convertMessageMethod.UserRecordPage(map));
          }
          /** 删除好友消息后 下次刷新不在出现在列表中 */
          if (map.getUrl().equals("delmsglistInfo")) {
            redisReceiver.receiveMessage(convertMessageMethod.delmsglistInfo(map));
          }
          /** 删除好友消息后 下次刷新不在出现在列表中 */
          if (map.getUrl().equals("UseridRecord")) {
            redisReceiver.receiveMessage(convertMessageMethod.UseridRecord(map));
          }
        }
        /** 心跳机制 */
        if (map.getCmd() == cmdEnum.ping.ordinal()) {
          redisReceiver.receiveMessage(convertMessageMethod.PingpongMap(map));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnError
  public void onError(Session session, Throwable error) {
    logger.debug("发生错误");
    error.printStackTrace();
  }
}
