package com.webim.im.controller;


import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.model.Enum.cmdEnum;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.webServer.WebUserServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@EnableScheduling
@ServerEndpoint(value = "/websocket/{userid}")
@Component
public class WebSocketServer { //每个人会分配一个独立的实例


  static WebUserServer webUserServer;
    @Autowired
    void  setWebUserServer(WebUserServer webUserServer){
        WebSocketServer.webUserServer=webUserServer;
    }
    static ObjectMapper objectMapper;
    @Autowired
    void  setObjectMapper(ObjectMapper objectMapper){
        WebSocketServer.objectMapper=objectMapper;
    }
     static RedisReceiver redisReceiver;
    @Autowired void setRedisReceiver(RedisReceiver redisReceiver) {
        WebSocketServer.redisReceiver=redisReceiver;
    }

    static ConvertMessageMethod convertMessageMethod;
    @Autowired
    void  setConvertMessageMethod(ConvertMessageMethod convertMessageMethod){
        WebSocketServer.convertMessageMethod=convertMessageMethod;
    }
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam("userid") Integer userid, Session session) throws Exception {
        System.out.println("建立成功");
        redisReceiver.onOpen(session,userid);
        // 记载用户初始化信息 连接后台后 发送给前端
        redisReceiver.sendToUserid(userid,webUserServer.init(userid));

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        redisReceiver.onClose(session);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            MessageBody map = objectMapper.readValue(message, MessageBody.class);
            if(map.getMsgtype()== msgtypeEnum.request.ordinal()) {
                if (map.getCmd() == cmdEnum.chat.ordinal()) { // 聊天记录
                    redisReceiver.onMessage(map, session);
                }
                if (map.getCmd() == cmdEnum.httprequest.ordinal()) { // 获取用户列表  查询用户列表
                    if (map.getUrl().equals("init")) {
                        redisReceiver.receiveMessage(convertMessageMethod.init(map));
                    }
                    if (map.getUrl().equals("getuserlist")) { // 测试用来 查看所有用户的 无用方法
                        redisReceiver.receiveMessage(convertMessageMethod.getuserlist(map));
                    }
                    if (map.getUrl().equals("getapplyfriendlist")) { // 获取好友申请列表
                        redisReceiver.receiveMessage(convertMessageMethod.getapplyfriendlist(map));
                    }
                    if (map.getUrl().equals("getfriedns")) {  // 添加到好友申请表
                        redisReceiver.receiveMessage(convertMessageMethod.getfriedns(map));
                    }
                    if (map.getUrl().equals("updApplyUser")) {  // 修改好友申请表 添加到好友表中
                        redisReceiver.receiveMessage(convertMessageMethod.updApplyUser(map));
                    }
                    if (map.getUrl().equals("findUserRead")) {  //  获取当前聊天对象的未读取的消息 并修改成已读状态
                        redisReceiver.receiveMessage(convertMessageMethod.findUserRead(map));
                    }
                    if (map.getUrl().equals("findUseridRecordCustom")) {  // 根据用户id 显示用户好友聊天列表
                        redisReceiver.receiveMessage(convertMessageMethod.findUseridRecordCustom(map));
                    }
                    if (map.getUrl().equals("updsign")) {  // 根据用户id  修改个性签名
                        redisReceiver.receiveMessage(convertMessageMethod.updsign(map));
                    }
                    if (map.getUrl().equals("getSSOIdUserAndRecord")) {  //  单击 用户Id   去SSO ID 获取用户信息 以及和对应的聊天记录
                        redisReceiver.receiveMessage(convertMessageMethod.getSSOIdUserAndRecord(map));
                    }
                    if (map.getUrl().equals("getlistUserName")) {  // 查询根据名称模糊查询好友列表
                        redisReceiver.receiveMessage(convertMessageMethod.getlistUserName(map));
                    }
                    if (map.getUrl().equals("delfriendAndRecord")) {  // 删除好友
                        redisReceiver.receiveMessage(convertMessageMethod.delfriendAndRecord(map));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




     @OnError
     public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
     }


}
