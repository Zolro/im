package com.webim.im.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Enum.User.UserEnum;
import com.webim.im.dao.RecordDao;
import com.webim.im.dao.UserDao;
import com.webim.im.entity.Record;
import com.webim.im.entity.User;
import com.webim.im.model.ContentBoby;
import com.webim.im.model.Enum.cmdEnum;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.view.Page;
import com.webim.im.webServer.WebUserServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.websocket.Session;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@EnableScheduling
@Service
@Transactional
public class RedisReceiver {

    @Autowired
    WebUserServer webuserServer;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserDao userDao;
    @Autowired
    RecordDao recordDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    EntityManager entityManager;


    public static class SessionInfo {
        public Session session;
        public Integer userid;

        public SessionInfo(Session session, Integer userid) {
            this.session = session;
            this.userid = userid;
        }
    }

    public List<SessionInfo> list = new LinkedList();

    // 打开连接 并获取上线之前接收到的离线 缓存到redis 中
    public void onOpen(Session session, Integer userid) {
        if (isUserOnline(userid)) {
            try {
                System.out.println("用户已登录");
                send(session, "用户已登录");
                session.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        synchronized (list) {
            list.add(new SessionInfo(session, userid));
            System.out.println("有人上线了：" + list.size());
            stringRedisTemplate.opsForValue().setBit(UserEnum.ONLINE.toString(), userid, true);
            sendOnlineOrOffline(userid, UserEnum.ONLINE.toString());
        }
        // 推送离线消息 以及最后一条消息信息
        System.out.println("推送消息给前端对应的用户");
        MessageBody messageBody= webuserServer.findUseridRecordCustom(userid,0,10);
        try {
            stringRedisTemplate.convertAndSend("chat", objectMapper.writeValueAsString(messageBody));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void onClose(Session session) {
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                SessionInfo info = list.get(i);
                if (info.session.getId().equals(session.getId())) {
                    stringRedisTemplate.opsForValue().setBit("ONLINE", info.userid, false); //标记为不在线
                    sendOnlineOrOffline(info.userid, "offline");
                    list.remove(info);
                    break;
                }
            }
            System.out.println("有人下线了：" + list.size());
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
        System.out.println("发送消息");
        try {
            session.getAsyncRemote().sendText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 判断该用户id 是否在线
    public boolean isUserOnline(Integer userid) {
        return stringRedisTemplate.opsForValue().getBit("ONLINE", userid);
    }

    /**
     * 接收消息的方法
     */
    public void receiveMessage(String message) {
        System.out.println("收到一条消息：" + message);
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
    // 推送离线收到消息
    public void  sendToUserid(Integer userid, MessageBody messageBody) throws Exception {
        if (isUserOnline(userid)) { //在线
            synchronized (list) {
                list.forEach(info -> {
                    if (info.userid == messageBody.getReceiver()) {
                        try {
                            send(info.session,objectMapper.writeValueAsString(messageBody));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void sendOnlineOrOffline(Integer userid, String status) {
        //通知大家
        userDao.findAllWhoFollwMe(userid).forEach(user -> {
            if (!isUserOnline(user.getId())) return;
            for (int n = 0; n < list.size(); n++) {
                SessionInfo si = list.get(n);
                if (si.userid == user.getId()) {
                    send(si.session, Result.of()
                            .put("type", status)
                            .put("userid", userid)
                            .toString());
                }
            }
        });
    }

    /**
     * 发消息给关注我的人，或者说是好友列表中包含我的人
     */
    public void sendToFollwMe(Integer userid, Result result) {
        //通知大家
        userDao.findAllWhoFollwMe(userid).forEach(user -> {
            if (!isUserOnline(user.getId())) return;
            for (int n = 0; n < list.size(); n++) {
                SessionInfo si = list.get(n);
                if (si.userid == user.getId()) {
                    send(si.session, result.toString());

                }
            }
        });
    }


    @Scheduled(fixedRate = 100000)  // 每隔多少秒执行1次
    public void refreshRedisOnlineInfo() {
        System.out.println("刷新redis在线数据(仅限于单机服务器，集群环境下会出错)");
        synchronized (list) {
            stringRedisTemplate.delete("ONLINE");
            for (int i = 0; i < list.size(); i++) {
                SessionInfo info = list.get(i);
                stringRedisTemplate.opsForValue().setBit("ONLINE", info.userid, true);
            }
        }
    }

    // 判断发送对象是否在线   在线 发送则存入redis 先发送一份给用户自己
    public void isUserOnlineSendrecord(Record record, MessageBody message, Session session) {
        MessageBody messageBody = message;
        try {
            try {
                messageBody.setContent(objectMapper.writeValueAsString(record));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            messageBody.setMsgtype(msgtypeEnum.response.ordinal());
            messageBody.setCmd(cmdEnum.chat.ordinal());
            if (isUserOnline(message.getReceiver())) {
                stringRedisTemplate.convertAndSend("chat", objectMapper.writeValueAsString(messageBody));
            }
            messageBody.setReceiver(messageBody.getSender());
            stringRedisTemplate.convertAndSend("chat", objectMapper.writeValueAsString(messageBody));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 新增在聊天记录表中发送数据
    public Record addrecord(MessageBody message, User user) {
        // 新增在聊天记录表中发送数据
        Record record = new Record();   // 发送者存储消息 给发送者拥有
        record.setFromId(message.getSender());
        record.setToId(message.getReceiver());
        record.setState(true); // 判断是否读取过
        record.setIssend(true); // true 代表是发送者 false 代表接受者
        record.setCreated(new Date());
        ContentBoby cb = null;
        try {
            cb = objectMapper.readValue(message.getContent(), ContentBoby.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        record.setContent(cb.getContent());
        record.setType(cb.getType());// 消息类型
        record = recordDao.save(record);


        Record torecord = new Record();  // 接受者存储消息 给接收者拥有
        torecord.setFromId(message.getReceiver());
        torecord.setToId(message.getSender());
        torecord.setState(false);
        torecord.setIssend(false);
        torecord.setCreated(new Date());
        torecord.setContent(cb.getContent());
        torecord.setType(cb.getType());
        recordDao.save(torecord);
        return record;
    }

    // 判断发送者是否登录
    public void isloginmessage(MessageBody message, Session session) {
        if (!isUserOnline(message.getSender())) {
            logicErrorClose(1, cmdEnum.offerror.ordinal(), 1, session);
        }
    }

    // 判断接收人是否存在
    public User isexits(Integer userid, Session session) {
        User user = userDao.findOne(userid);
        if (user == null) {
            logicErrorClose(1, cmdEnum.offerror.ordinal(), 2, session);
        }
        return user;
    }

    // 逻辑错误 返回发送消息给前端后关闭session
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
