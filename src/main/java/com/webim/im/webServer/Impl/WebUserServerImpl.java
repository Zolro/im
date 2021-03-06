package com.webim.im.webServer.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.module.entity.User;
import com.webim.im.module.server.UserServer;
import com.webim.im.model.Enum.cmdEnum;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.utils.Result;
import com.webim.im.webServer.WebUserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WebUserServerImpl implements WebUserServer {
  @Autowired UserServer userServer;
  @Autowired ObjectMapper objectMapper;

  @Override
  public MessageBody init(Integer userid) {
    return packagingulrpublic(
        getMethodName(), convertObejctToStringTOResult(userServer.init(userid)), userid);
  }

  @Override
  public MessageBody updsign(Integer userid, String sign) {
    return packagingulrpublic(
        getMethodName(), convertObejctToStringTOResult(userServer.updsign(userid, sign)), userid);
  }

  @Override
  public MessageBody findUseridRecordCustom(Integer userid, Integer start, Integer limit) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.findUseridRecordCustom(userid, start, limit)),
        userid);
  }

  @Override
  public MessageBody getfriends(
      Integer userid, Integer touserId, Integer groupiden, Integer type, String postscript) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToString(userServer.getfriends(userid, touserId, groupiden, type, postscript)),
        userid);
  }

  @Override
  public MessageBody getListUserName(Integer userid, String name) {
    return packagingulrpublic(
        getMethodName(), convertObejctToString(userServer.getlistNameUser(userid, name)), userid);
  }

  @Override
  public MessageBody getListUserNameFriend(Integer userid, String name) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToString(userServer.getlistUserNamefriend(userid, name)),
        userid);
  }

  @Override
  public MessageBody findByTopic(String username) {
    //       return
    // packagingulrpublic(getMethodName(),convertObejctToStringTOResult(userServer.findByTopic(username)));
    return null;
  }

  @Override
  public MessageBody getUserList(Integer userid) {
    return packagingulrpublic(
        getMethodName(), convertObejctToStringTOResult(userServer.getuserlist(userid)), userid);
  }

  @Override
  public MessageBody getSSOIdUserAndRecord(Integer userid, Integer topic) {
    User user= userServer.findById(userid);
    if(user.getTopic().equals(topic)){
      String obj = null;
      try {
        obj = objectMapper.writeValueAsString(Result.of(0, "不可和自己聊天", ""));
        return packagingulrpublic(
                getMethodName(),
                obj,
                userid);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.getSSOIdUserAndRecord(userid, topic)),
        userid);
  }

  @Override
  public MessageBody getApplyFriendList(Integer userid) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.getapplyfriendlist(userid)),
        userid);
  }

  @Override
  public MessageBody updApplyUser(Integer from,
      Integer applyuserid, Integer state, String reply, Integer groupid) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.updApplyUser(applyuserid, state, reply, groupid)),from);
  }

  @Override
  public MessageBody findUserRead(Integer formuserid, Integer touserid) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.findUserRead(formuserid, touserid)),
        formuserid);
  }
  //  获取当前方法的方法名称
  // getStackTrace 对应的list   0 getStackTrace   1 代表 getMethodName这个方法本身
  // 2代表调用当前这个方法（getMethodName）的方法名称
  @Override
  public String getMethodName() {
    return Thread.currentThread().getStackTrace()[2].getMethodName();
  }

  @Override
  public String convertObejctToString(Object object) {
    String obj = null;
    try {
      obj = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return obj;
  }

  @Override
  public MessageBody delFriendAndRecord(Integer userid, Integer friendid) {
    if (userServer.delfriendAndRecord(userid, friendid)) {
      return packagingulrpublic(getMethodName(), convertObejctToStringTOResult(""), userid);
    } else {
      String obj = null;
      try {
        obj = objectMapper.writeValueAsString(Result.of(0, "失败", ""));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      return packagingulrpublic(getMethodName(), obj,userid);
    }
  }

  @Override
  public MessageBody UserRecordPage(
      Integer fromid,
      Integer toid,
      Integer start,
      Integer limit,
      String slursearch,
      Date starttime) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(
            userServer.UserRecordPage(fromid, toid, start, limit, slursearch, starttime)),
        fromid);
  }

  @Override
  public MessageBody pageNowRecord(Integer from, Integer to, Integer start, Integer limit) {
    return packagingulrpublic(
            getMethodName(),
            convertObejctToStringTOResult(
                    userServer.pageNowRecord(from,to,start,limit)),from);
  }

  @Override
  public MessageBody delMsgListInfo(Integer fromid, Integer recordid) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.delmsglistInfo(recordid)),
        fromid);
  }

  @Override
  public MessageBody UseridRecord(Integer userid, Integer friendid) {
    return packagingulrpublic(
        getMethodName(),
        convertObejctToStringTOResult(userServer.UseridRecord(userid, friendid)),
        userid);
  }

  // 这个方法额外多了追加返回结果
  public String convertObejctToStringTOResult(Object object) {
    String obj = null;
    try {
      obj = objectMapper.writeValueAsString(Result.of(200, "成功", object));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return obj;
  }
  // 把对应的server方法返回的指 转换成 MessageBody  参数1 url 方法名称 参数2 返回结果 参数3 接受者ID
  @Override
  public MessageBody packagingulrpublic(String url, String content, Integer userId) {
    MessageBody messageBody = new MessageBody();
    messageBody.setMsgtype(msgtypeEnum.response.ordinal());
    messageBody.setCmd(cmdEnum.httprequest.ordinal());
    messageBody.setReceiver(userId);
    messageBody.setUrl(url);
    messageBody.setContent(content);
    return messageBody;
  }
}
