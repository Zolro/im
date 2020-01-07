package com.webim.im.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Enum.User.UserEnum;
import com.webim.im.module.dao.RecordDao;
import com.webim.im.module.dao.UserDao;
import com.webim.im.module.entity.Record;
import com.webim.im.module.entity.User;
import com.webim.im.model.ContentBoby;
import com.webim.im.model.Enum.cmdEnum;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.webServer.WebUserServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@Service
@Transactional
@Slf4j
public class RedisReceiver {
  @Autowired WebUserServer webuserServer;
  @Autowired StringRedisTemplate stringRedisTemplate;
  @Autowired UserDao userDao;
  @Autowired RecordDao recordDao;
  @Autowired private ObjectMapper objectMapper;
  @Autowired EntityManager entityManager;

  public static class SessionInfo {
    public Integer userid;
    public String token;
    public SessionInfo(Integer userid, String token) {
      this.userid = userid;
      this.token = token;
    }
  }

  /** 采用ConcurrentHashMap类,细化锁颗粒度 */
  private ConcurrentHashMap<Integer, List<Session>> userOnlines = new ConcurrentHashMap();
  private ConcurrentHashMap<Session, SessionInfo> userMap = new ConcurrentHashMap();
  public void onOpen(Session session, Integer userid, String token) {
    List<Session> list=userOnlines.get(userid);
    if(list==null){
      list=new ArrayList<>();
      list.add(session);
      userOnlines.put(userid,list);
      stringRedisTemplate.opsForValue().setBit(UserEnum.ONLINE.toString(), userid, true);
      sendOnlineOrOffline(userid, UserEnum.ONLINE.toString());
      log.debug("有人上线了：{}", userOnlines.keySet().size());
    }else{
      list.add(session);
    }
    SessionInfo infolist= userMap.get(session);
    if(infolist==null){
      infolist=(new SessionInfo(userid, token));
      userMap.put(session,infolist);
    }
  }

  public void onClose(Session session) {
    SessionInfo info=userMap.get(session);
    if(userMap.get(session)!=null){
      if(userOnlines.get(info.userid).size()==1){
        stringRedisTemplate.opsForValue().setBit("ONLINE", info.userid, false); // 标记为不在线
        sendOnlineOrOffline(info.userid, UserEnum.OFFLINE.toString());
        log.debug("有人下线了：{}", userOnlines.keySet().size());
      }
      userMap.remove(session);
    }
  }

  // 发送消息 后对消息进行处理
  public void onMessage(MessageBody message, Session session) {
    // 判断发送者是否登录
    isloginmessage(message, session);
    // 判断接收人是否存在
    User user = isexits(message.getReceiver(), session);
    // 新增在聊天记录表中发送数据
    Record record = addrecord(message, user);
    // 判断发送对象是否在线   在线 发送则存入redis
    isUserOnlineSendrecord(record, message, session);
  }
  // 发送消息给前端
  public void send(Session session, String text) {
    log.info("发送消息");
    try {
      if(session.isOpen()){
        session.getAsyncRemote().sendText(text);
      }else {
        log.info("session通道已关闭");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 判断该用户id 是否在线
  public boolean isUserOnline(Integer userid) {
    return stringRedisTemplate.opsForValue().getBit("ONLINE", userid);
  }
//  // 判断该用户id 是否在线
//  public boolean isUserOnline(Integer userid, Session session) {
//    return stringRedisTemplate.opsForValue().getBit("ONLINE", userid);
//  }

  /** 接收消息的方法 */
  public void receiveMessage(String message) {
    System.err.println("收到一条消息：" + message);
    try {
      MessageBody messageBody = objectMapper.readValue(message, MessageBody.class);
      sendToUserid(messageBody.getReceiver(), messageBody);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void receiveMessage(MessageBody message) {
    try {
      sendToUserid(message.getReceiver(), message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /** 推送离线收到消息 */
  public void sendToUserid(Integer userid, MessageBody messageBody) throws Exception {
    /** 在线 */
    if (isUserOnline(userid)) {
      List<Session> list =userOnlines.get(messageBody.getReceiver());
      if(list!=null){
        for (int i = 0; i <list.size() ; i++) {
          if(null!=messageBody.getUrl()){
            if(list.size()>=2){  // 这里操作当有同一用户从多个网页中登录 避免初始化和拉去好友信息多次发送
              if(messageBody.getUrl().equals("init")&& i!=list.size()-1){
                continue;
              }
              if(messageBody.getUrl().equals("findUseridRecordCustom")&& i!=list.size()-1){
                continue;
              }
            }
          }
          send(list.get(i),objectMapper.writeValueAsString(messageBody));
        }
      }
    }
  }

  public void sendOnlineOrOffline(Integer userid, String status) {
    /** 通知大家 */
    userDao
        .findAllWhoFollwMe(userid)
        .forEach(
            user -> {
              if (!isUserOnline(user.getId())) {
                return;
              }
              List<Session> list =userOnlines.get(user.getId());
              if(list!=null){
                for (int i = 0; i <list.size() ; i++) {
                  send(list.get(i), Result.of().put("type", status).put("userid", userid).toString());
                }
              }
            });
  }

  /** 发消息给关注我的人，或者说是好友列表中包含我的人 */
  public void sendToFollwMe(Integer userid, Result result) {
    // 通知大家
    userDao
        .findAllWhoFollwMe(userid)
        .forEach(
            user -> {
              if (!isUserOnline(user.getId())) {
                return;
              }
              List<Session> list =userOnlines.get(user.getId());
              if(list!=null){
                for (int i = 0; i <list.size() ; i++) {
                  send(list.get(i), result.toString());
                }
              }
            });
  }

  @Scheduled(fixedRate = 100) // 每隔多少秒执行1次
  public void refreshRedisOnlineInfo() {
    log.debug("刷新redis在线数据(仅限于单机服务器，集群环境下会出错)");
    stringRedisTemplate.delete("ONLINE");
    for (Integer value : userOnlines.keySet()) {
      stringRedisTemplate.opsForValue().setBit("ONLINE", value, true);
    }
  }

  // 判断发送对象是否在线   在线 发送则存入redis 先发送一份给用户自己
  public void isUserOnlineSendrecord(Record record, MessageBody message, Session session) {
    MessageBody messageBody = message;
    try {
      messageBody.setMsgtype(msgtypeEnum.response.ordinal());
      messageBody.setCmd(cmdEnum.chat.ordinal());
      Boolean issend = record.getIssend();
      if (isUserOnline(message.getReceiver())) {
        record.setIssend(!issend);
        messageBody.setContent(objectMapper.writeValueAsString(record));
        stringRedisTemplate.convertAndSend("chat", objectMapper.writeValueAsString(messageBody));
      }
      record.setIssend(issend);
      messageBody.setContent(objectMapper.writeValueAsString(record));
      messageBody.setReceiver(messageBody.getSender());
      stringRedisTemplate.convertAndSend("chat", objectMapper.writeValueAsString(messageBody));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  // 新增在聊天记录表中发送数据
  public Record addrecord(MessageBody message, User user) {
    // 新增在聊天记录表中发送数据
    /** 发送者存储消息 给发送者拥有 */
    Record record = new Record();
    record.setFromId(message.getSender());
    record.setToId(message.getReceiver());
    /** 判断是否读取过 */
    record.setState(true);
    /** true 代表是发送者 false 代表接受者 */
    record.setIssend(true);
    record.setCreated(new Date());
    record.setSigndel(false);
    ContentBoby cb = null;
    try {
      cb = objectMapper.readValue(message.getContent(), ContentBoby.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    record.setContent(cb.getContent());
    /** 消息类型 */
    record.setType(cb.getType());
    record = recordDao.save(record);

    /** 接受者存储消息 给接收者拥有 */
    Record torecord = new Record();
    torecord.setFromId(message.getReceiver());
    torecord.setToId(message.getSender());
    torecord.setState(false);
    torecord.setIssend(false);
    torecord.setCreated(new Date());
    torecord.setContent(cb.getContent());
    torecord.setType(cb.getType());
    torecord.setSigndel(false);
    recordDao.save(torecord);
    return record;
  }

  /** 判断发送者是否登录 */
  public void isloginmessage(MessageBody message, Session session) {
    if (!isUserOnline(message.getSender())) {
      logicErrorClose(1, cmdEnum.offerror.ordinal(), 1, session);
    }
  }

  /** 判断接收人是否存在 */
  public User isexits(Integer userid, Session session) {
    User user = userDao.findOne(userid);
    if (user == null) {
      logicErrorClose(1, cmdEnum.offerror.ordinal(), 2, session);
    }
    return user;
  }

  /** 逻辑错误 返回发送消息给前端后关闭session */
  public void logicErrorClose(Integer msgtype, Integer cmd, Integer errorcode, Session session) {
    MessageBody mb = new MessageBody();
    mb.setMsgtype(msgtype);
    mb.setCmd(cmd);
    mb.setErrorcode(errorcode);
    try {
      send(session, objectMapper.writeValueAsString(mb));
      session.close();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException io) {
    }
  }
}
