package com.webim.im.Server;

import java.util.List;

import com.webim.im.Server.SererImpl.view.TemporaryUserinfo;
import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.Record;
import com.webim.im.entity.User;
import com.webim.im.view.Page;
// 用户Server 接口
public interface UserServer {
    // 用户登录初始化数据
    Object init(Integer userid);
    // 用户注册
    Object createUser(Integer userCenterID, String username, String sign, String imageUrl);
    // 用户注册 返回用户
    User createtoUser(Integer userCenterID, String username, String sign, String imageUrl);
    /**
     * @Author zw
     * @Description IM前端向后端获取用户ID  如果对应的用户ID不存在则创建用户
     * @Date 10:41 2019/8/21
     * @Param
     **/
    Integer getImUserInfo(Integer userCenterID, String username);
    // 修改个性签名信息
    User updsign(Integer userid,String  sign);

    // 添加好友
    Object getfriedns(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript);
    User findByTopic(Integer userCenterID);
    // 获取用户列表 测试用方法
    List<UserViews> getuserlist(Integer userid);

    // 根据用户id  查询收到好友申请列表
    List<ApplyUserListView> getapplyfriendlist(Integer userid);
    // 修改好友申请列表
    ApplyUser updApplyUser(Integer applyuserid,Integer state,String reply,Integer groupid);
    /**
     * @Author zw
     * @Description  获取当前聊天对象的未读取的消息 并修改成已读状态
     * @Date 20:33 2019/8/13
     * @Param
     **/
    List<Record> findUserRead(Integer formuserid,Integer touserid);
    /**
     * @Author zw
     * @Description  根据用户id 查询聊天记录 根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
     * @Date 21:00 2019/8/14
     * @Param
     **/
    Page findUseridRecordCustom(Integer userid, Integer start, Integer limit);

    /**
     * @Author zw
     * @Description    获取SSO ID 获取用户信息 以及和对应的聊天技能
     * @Date 11:22 2019/8/28
     * @Param formuserid 当前登录的用户信息
     **/
    TemporaryUserinfo getSSOIdUserAndRecord(Integer formuserid,Integer topic);

    /**
     * @Author zw
     * @Description 查询根据名称模糊查询好友列表 或者消息列表
     * @Date 15:52 2019/8/28
     * @Param
     **/
    List<User> getlistNameUser(Integer userid, String name);

    /**
     * @Author zw
     * @Description  删除好友和聊天
     * @Date 16:33 2019/8/28
     * @Param
     **/
    Boolean delfriendAndRecord(Integer userid, Integer friendid);

}
