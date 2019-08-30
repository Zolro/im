package com.webim.im.webServer;

import java.util.List;

import com.webim.im.entity.User;
import com.webim.im.model.MessageBody;
import com.webim.im.view.Page;

import org.springframework.web.multipart.MultipartFile;

public interface WebUserServer {
    // 用户登录初始化数据
    MessageBody init(Integer userid);
    // 修改个性签名信息
    MessageBody updsign(Integer userid, String  sign);
    /**
     * @Author zw
     * @Description  根据用户id 显示用户好友聊天列表     根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
     * @Date 10:10 2019/8/15
     * @Param
     **/
    MessageBody findUseridRecordCustom(Integer userid, Integer start, Integer limit);
    /**
     * @Author zw
     * @Description 添加好友
     * @Date 11:01 2019/8/28
     * @Param
     **/
    MessageBody getfriedns(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript);

    /**
     * @Author zw
     * @Description 查询根据名称模糊查询消息列表
     * @Date 15:52 2019/8/28
     * @Param
     **/
    MessageBody getlistUserName(Integer userid, String name);
    /**
     * @Author zw
     * @Description 查询根据名称模糊查询好友列表
     * @Date 15:52 2019/8/28
     * @Param
     **/
    MessageBody getlistUserNamefriend(Integer userid, String name);
    /**
     * @Author zw
     * @Description  根据用户拉去对应的用户信息
     * @Date 11:02 2019/8/28
     * @Param
     **/
    MessageBody findByTopic(String username);
    /**
     * @Author zw
     * @Description 获取用户列表
     * @Date 11:01 2019/8/28
     * @Param
     **/
    MessageBody getuserlist(Integer userid);
    /**
     * @Author zw
     * @Description 获取SSO ID 获取用户信息 以及和对应的聊天技能
     * @Date 11:18 2019/8/28
     * @Param
     **/
    MessageBody getSSOIdUserAndRecord(Integer userid, Integer topic);
   /**
    * @Author zw
    * @Description 根据用户id  查询收到好友申请列表
    * @Date 11:01 2019/8/28
    * @Param
    **/
    MessageBody getapplyfriendlist(Integer userid);
    /**
     * @Author zw
     * @Description 修改好友申请列表
     * @Date 11:02 2019/8/28
     * @Param
     **/
    MessageBody updApplyUser(Integer applyuserid, Integer state, String reply, Integer groupid);
    /**
     * @Author zw
     * @Description  获取当前聊天对象的未读取的消息 并修改成已读状态
     * @Date 20:33 2019/8/13
     * @Param
     **/
    MessageBody findUserRead(Integer formuserid, Integer touserid);
    /**
     * @Author zw
     * @Description  封装返回结果给前端
     * @Date 15:08 2019/8/14
     * @Param
     **/
    MessageBody packagingulrpublic(String url,String content,Integer userid);
    MessageBody packagingulrpublic(String url,String content);
    /**
     * @Author zw
     * @Description  获取当前方法的名称;
     * @Date 15:14 2019/8/14
     * @Param
     **/
    String getMethodName();
    /**
     * @Author zw
     * @Description  将对象转换成String
     * @Date 15:09 2019/8/14
     * @Param
     **/
    String convertObejctToString(Object object);

    /**
     * @Author zw
     * @Description  删除好友
     * @Date 16:39 2019/8/28
     * @Param
     **/
    MessageBody delfriendAndRecord(Integer userid, Integer friendid);

    /**
     * @Author zw
     * @Description 好友聊天记录
     * @Date 14:12 2019/8/29
     * @Param
     **/
    MessageBody UserRecordPage(Integer fromid, Integer toid, Integer start, Integer limit);
}
