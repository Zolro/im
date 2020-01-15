package com.webim.im.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.model.Enum.cmdEnum;
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
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user= getUserId(map);
        MessageBody body=null;
        if(user!=null){
            body= webUserServer.init(user);
        }else{
            body =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }
        return body;
    }

    public MessageBody PingpongMap(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Object ping= map.get("ping");
        Object userid= map.get("userid");
        MessageBody body=null;
        if(ping=="ping"){
            body=new MessageBody();
            body.setMsgtype(msgtypeEnum.response.ordinal());
            body.setCmd(cmdEnum.ping.ordinal());
            body.setReceiver(Integer.valueOf(String.valueOf(userid)));
            body.setUrl("PingpongMap");
            body.setContent("pong");
        }
        return body;
    }
    /**
     * @Author zw
     * @Description 历史好友聊天记录
     * @Date 14:14 2019/8/29
     * @Param
     **/
    public MessageBody UserRecordPage(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer from= getForm(map);
        Integer to= getTo(map);
        Integer start= getStart(map);
        Integer limit= getLimit(map);
        Object starttime= map.get("starttime");
        Object slursearch= map.get("slursearch");
        MessageBody body=null;
        if(from==null||to==null||limit==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空,<<fromid,toid,limit>>");
        }else{
            Date starttimeOP = null;
            //先将要格式化的字符串转为Date类型
            if(starttime!=null){
                try {
                    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                    starttimeOP = dateFormat.parse(String.valueOf(starttime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            body= webUserServer.UserRecordPage(from,to,start,limit,slursearch.toString(),starttimeOP);
        }
        return body;
    }

    /**
     * @Author zw
     * @Description 当前好友聊天记录
     * @Date 15:23 2020/1/14
     * @Param
     **/
    public MessageBody pageNowRecord(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer from = getForm(map);
        Integer to= getTo(map);
        Integer start=getStart(map);
        Integer limit= getLimit(map);
        MessageBody body=null;
        if(from==null||to==null||limit==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空,<<from，to，limit>>");
        }else{
            body= webUserServer.pageNowRecord(from,to,start,limit);
        }
        return  body;
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
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user= getUserId(map);
        Integer friend= getFriendId(map);
        MessageBody body=null;
        if(user==null||friend==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空,,<<userid,friendid>>");
        }else {
            body= webUserServer.delFriendAndRecord(user,friend);
        }
        return body;
    }


    // 查询 不是本好友的所有用户列表
    public MessageBody getuserlist(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user= getUserId(map);
        MessageBody body=null;
        if(user!=null){
            body= webUserServer.getUserList(user);
        }else{
            body =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }
        return body;
    }
    // 查询申请了好友的列表
    public MessageBody getapplyfriendlist(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user= getUserId(map);
        MessageBody body=null;
        if(user!=null){
            body= webUserServer.getApplyFriendList(user);
        }else{
            body =packageResult(messageBody.getReceiver(),0,"参数userid不可为空");
        }
        return body;
    }
    public MessageBody findUserRead(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Object form= map.get("formuserid");
        Object to= map.get("touserid");
        MessageBody body=null;
        if(form==null){
            body =packageResult(messageBody.getReceiver(),0,"参数formuserid不可为空");
        }else if(to==null){
            body =packageResult(messageBody.getReceiver(),0,"参数touserid不可为空");
        }else{
            body= webUserServer.findUserRead(Integer.valueOf(String.valueOf(form)
                    ),Integer.valueOf(String.valueOf(to)));
        }
        return body;
    }
    // 根据用户id 显示用户好友聊天列表     根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
    public MessageBody findUseridRecordCustom(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer userid= getUserId(map);
        Integer start= getStart(map);
        Integer limit= getLimit(map);
        MessageBody body=null;
         if(limit==null){
             body =packageResult(messageBody.getReceiver(),0,"参数limit不可为空");
        }else{
             body= webUserServer.findUseridRecordCustom(userid,start,limit);
        }
        return body;
    }

    public MessageBody getlistUserName(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer userid= getUserId(map);
        Object username= map.get("name");
        MessageBody body=null;
        if(userid==null||username==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空,<<userid,name>>");
        }else{
            body= webUserServer.getListUserName(userid,String.valueOf(username));
        }
        return body;
    }
    public MessageBody delmsglistInfo(MessageBody messageBody) {
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer form= getForm(map);
        Object recordid= map.get("recordid");
        MessageBody body=null;
        if(form==null||recordid==null){
            body =packageResult(messageBody.getReceiver(),0," 必传参数不可为空，<<fromid，recordid>>");
        }else{
            body= webUserServer.delMsgListInfo(form,Integer.valueOf(String.valueOf(recordid)));
        }
        return body;
    }
    public MessageBody UseridRecord(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user= getUserId(map);
        Integer friend= getFriendId(map);
        MessageBody body=null;
        if(user==null||friend==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空，<<userid，friendid>>");
        }else{
            body= webUserServer.UseridRecord(user,friend);
        }
        return body;
    }
    public MessageBody getlistUserNamefriend(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer user=getUserId(map);
        Object username= map.get("name");
        MessageBody body=null;
        if(user==null||username==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空，<<userid，name>>");
        }else{
            body= webUserServer.getListUserNameFriend(user,String.valueOf(username));
        }
        return body;
    }

    public  MessageBody updApplyUser(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Object applyuserid= map.get("applyuserid");
        Object state= map.get("state");
        Object reply= map.get("reply");
        Object groupid= map.get("groupid");
        Integer from= getForm(map);
        MessageBody body=null;
        if(applyuserid==null||state==null||groupid==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空，<<applyuserid，stateId,groupid>>");
        }else{
            Integer auapplyuserid=Integer.valueOf(String.valueOf(applyuserid));
            Integer austate=Integer.valueOf(String.valueOf(state));
            String aureply=String.valueOf(reply);
            Integer augroupid=Integer.valueOf(String.valueOf(groupid));
            body= webUserServer.updApplyUser(from,auapplyuserid,austate,aureply,augroupid);
            body.setReceiver(messageBody.getReceiver());
        }
        return body;
    }
    //  添加好友申请
    public MessageBody getfriends(MessageBody messageBody){
        Map<String,Object> map=convertObejctToMap(messageBody.getContent());
        Integer formId= getForm(map);
        Object touserId= map.get("touserId");
        Object groupiden= map.get("groupiden");
        Object type= map.get("type");
        Object postscript= map.get("postscript");
        MessageBody body=null;
        if(formId==null||touserId==null||groupiden==null||type==null){
            body =packageResult(messageBody.getReceiver(),0,"必传参数不可为空，<<formId，touserId,groupiden,type>>");
        }else{
            Integer autouserId=Integer.valueOf(String.valueOf(touserId));
            Integer augroupiden=Integer.valueOf(String.valueOf(groupiden));
            Integer autype=Integer.valueOf(String.valueOf(type));
            body= webUserServer.getfriends(formId,autouserId,augroupiden,autype,String.valueOf(postscript));
            body.setReceiver(messageBody.getReceiver());
        }
        return body;
    }
    //  返回结果抽出  receiverid 接受者ID  code 返回状态码  msg 返回消息
    private MessageBody packageResult(Integer receiverid,Integer code,String msg){
        String name= Thread.currentThread() .getStackTrace()[2].getMethodName();
        String data=webUserServer.convertObejctToString( Result.of(code,msg)) ;
        Integer receiver=receiverid;
        return  webUserServer.packagingulrpublic(name,data,receiver);
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


    private  Integer  getFriendId(Map<String,Object> map){
        Object friend= map.get("friendid");
        Integer value=null;
        if(friend!=null){
            value= Integer.valueOf(String.valueOf(friend));
        }
        return value;
    }
    private  Integer  getUserId(Map<String,Object> map){
        Object user= map.get("userid");
        Integer value=null;
        if(user!=null){
            value= Integer.valueOf(String.valueOf(user));
        }
        return value;
    }
    private  Integer  getForm(Map<String,Object> map){
        Object from= map.get("fromid");
        Integer value=null;
        if(from!=null){
            value= Integer.valueOf(String.valueOf(from));
        }
        return value;
    }
    private  Integer  getTo(Map<String,Object> map){
        Object to= map.get("toid");
        Integer value=null;
        if(to!=null){
            value= Integer.valueOf(String.valueOf(to));
        }
        return value;
    }
    private  Integer  getLimit(Map<String,Object> map){
        Object limit= map.get("limit");
        Integer value=null;
        if(limit!=null){
            value= Integer.valueOf(String.valueOf(limit));
        }
        return value;
    }
    private  Integer  getStart(Map<String,Object> map){
        Object start= map.get("start");
        Integer value=null;
        if(start!=null){
            value= Integer.valueOf(String.valueOf(start));
        }
        return value;
    }

}
