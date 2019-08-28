package com.webim.im.controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.model.Enum.msgtypeEnum;
import com.webim.im.model.MessageBody;
import com.webim.im.utils.Result;
import com.webim.im.webServer.WebUserServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @Author zw
 * @Description  将方法的返回结果 加上Result  转换成MessageBody 返回给前端
 * @Date 15:20 2019/8/14
 * @Param
 **/
@Service
public class ConvertMessageMethod {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebUserServer webUserServer;
    public MessageBody init(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        MessageBody messageBody1=null;
        if(userid!=null){
            messageBody1= webUserServer.init(Integer.valueOf(String.valueOf(userid)));
        }else{
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }
        return messageBody1;
    }
    public MessageBody updsign(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        Object sign= mapOm.get("sign");
        MessageBody messageBody1=null;
        if(userid!=null){
            messageBody1= webUserServer.updsign(Integer.valueOf(String.valueOf(userid)),String.valueOf(sign));
        }
        return messageBody1;
    }
    public MessageBody getSSOIdUserAndRecord(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        Object topic= mapOm.get("topic");
        MessageBody messageBody1=null;
        if(userid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }else if(topic==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数topic不可为空");
        }else {
            messageBody1= webUserServer.getSSOIdUserAndRecord(Integer.valueOf(String.valueOf(userid)),Integer.valueOf(String.valueOf(topic)));
        }
        return messageBody1;
    }
    public MessageBody delfriendAndRecord(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        Object friendid= mapOm.get("friendid");
        MessageBody messageBody1=null;
        if(userid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }else if(friendid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数friendid不可为空");
        }else {
            messageBody1= webUserServer.delfriendAndRecord(Integer.valueOf(String.valueOf(userid)),Integer.valueOf(String.valueOf(friendid)));
        }
        return messageBody1;
    }


    // 查询 不是本好友的所有用户列表
    public MessageBody getuserlist(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        MessageBody messageBody1=null;
        if(userid!=null){
            messageBody1= webUserServer.getuserlist(Integer.valueOf(String.valueOf(userid)));
        }else{
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }
        return messageBody1;
    }
    // 查询申请了好友的列表
    public MessageBody getapplyfriendlist(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        MessageBody messageBody1=null;
        if(userid!=null){
            messageBody1= webUserServer.getapplyfriendlist(Integer.valueOf(String.valueOf(userid)));
        }else{
            messageBody1 =packageResult(messageBody.getReceiver(),0,"userid不可为空");
        }
        return messageBody1;
    }
    public MessageBody findUserRead(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object formuserid= mapOm.get("formuserid");
        Object touserid= mapOm.get("touserid");
        MessageBody messageBody1=null;
        if(formuserid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数formuserid不可为空");
        }else if(touserid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数touserid不可为空");
        }else{
            messageBody1= webUserServer.findUserRead(Integer.valueOf(String.valueOf(formuserid)
                    ),Integer.valueOf(String.valueOf(touserid)));
        }
        return messageBody1;
    }
    // 根据用户id 显示用户好友聊天列表     根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
    public MessageBody findUseridRecordCustom(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        Object start= mapOm.get("start");
        Object limit= mapOm.get("limit");
        MessageBody messageBody1=null;
        if(start==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数start不可为空");
        }else if(limit==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"参数limit不可为空");
        }else{
            messageBody1= webUserServer.findUseridRecordCustom(Integer.valueOf(String.valueOf(userid)
            ),Integer.valueOf(String.valueOf(start)),Integer.valueOf(String.valueOf(limit)));
        }
        return messageBody1;
    }

    public MessageBody getlistUserName(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object userid= mapOm.get("userid");
        Object username= mapOm.get("name");
        MessageBody messageBody1=null;
        if(userid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"userid不可为空");
        }else if(username==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"name不可为空");
        }else{
            messageBody1= webUserServer.getlistUserName(Integer.valueOf(String.valueOf(userid)),String.valueOf(username));
        }
        return messageBody1;
    }

    public  MessageBody updApplyUser(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object applyuserid= mapOm.get("applyuserid");
        Object state= mapOm.get("state");
        Object reply= mapOm.get("reply");
        Object groupid= mapOm.get("groupid");
        MessageBody messageBody1=null;
        if(applyuserid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"applyuserid不可为空");
        }else if(state==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"stateId");
        }else if(groupid==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"groupid不可为空");
        }else{
            Integer auapplyuserid=Integer.valueOf(String.valueOf(applyuserid));
            Integer austate=Integer.valueOf(String.valueOf(state));
            String aureply=String.valueOf(reply);
            Integer augroupid=Integer.valueOf(String.valueOf(groupid));
            messageBody1= webUserServer.updApplyUser(auapplyuserid,austate,aureply,augroupid);
            messageBody1.setReceiver(messageBody.getReceiver());
        }
        return messageBody1;
    }
    //  添加好友申请
    public MessageBody getfriedns(MessageBody messageBody){
        Map<String,Object> mapOm=convertObejctToMap(messageBody.getContent());
        Object formId= mapOm.get("formId");
        Object touserId= mapOm.get("touserId");
        Object groupiden= mapOm.get("groupiden");
        Object type= mapOm.get("type");
        Object postscript= mapOm.get("postscript");
        MessageBody messageBody1=null;
        if(formId==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"formId不可为空");
        }else if(touserId==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"touserId");
        }else if(groupiden==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"groupiden不可为空");
        }else if(type==null){
            messageBody1 =packageResult(messageBody.getReceiver(),0,"type不可为空");
        }else{
            Integer auformId=Integer.valueOf(String.valueOf(formId));
            Integer autouserId=Integer.valueOf(String.valueOf(touserId));
            Integer augroupiden=Integer.valueOf(String.valueOf(groupiden));
            Integer autype=Integer.valueOf(String.valueOf(type));
            String aupostscript=String.valueOf(postscript);
            messageBody1= webUserServer.getfriedns(auformId,autouserId,augroupiden,autype,aupostscript);
            messageBody1.setReceiver(messageBody.getReceiver());
        }
        return messageBody1;
    }
    //  返回结果抽出  receiverid 接受者ID  code 返回状态码  msg 返回消息
    private MessageBody packageResult(Integer receiverid,Integer code,String msg){
        String name= Thread.currentThread() .getStackTrace()[2].getMethodName();
        String data=webUserServer.convertObejctToString( Result.of(code,msg)) ;
        Integer receiver=receiverid;
        return  webUserServer.packagingulrpublic(name,data,receiver);
    }

    // 把消息转换成Map
    private  Map<String,Object>  convertToMap(MessageBody messageBody ){
        Map<String,Object> obj=null;
        try {
            obj=objectMapper.readValue(messageBody.getContent(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
    // 把对象转换成Map
    private  Map<String,Object>  convertObejctToMap(String object ){
        Map<String,Object> obj=null;
        try {
            obj=objectMapper.readValue(object, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
    // 把字符串转换成消息体
    private  MessageBody convertToMessage(String  object){
        MessageBody obj=null;
        try {
            obj=objectMapper.readValue(object,MessageBody.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
    private Object convertStringToObject(String object){
        Object ob=null;
        try {
            ob= objectMapper.readValue(object,Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ob;
    }

}
