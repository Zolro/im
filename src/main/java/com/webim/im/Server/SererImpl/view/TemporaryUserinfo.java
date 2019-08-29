package com.webim.im.Server.SererImpl.view;

import java.util.List;

import com.webim.im.entity.Record;

import lombok.Data;
import redis.clients.jedis.BinaryClient;

/**
 * @Author zw
 * @Description 获取临时用户信息
 * @Date 11:29 2019/8/28
 * @Param
 **/
@Data
public class TemporaryUserinfo {
    String  username;
    Integer id;
    String  avatar;
    String  sign;
    Integer  status;
    List<Record> list; //好友聊天记录
}
