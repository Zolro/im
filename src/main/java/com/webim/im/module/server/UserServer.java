package com.webim.im.module.server;

import java.util.Date;
import java.util.List;

import com.webim.im.module.dao.custom.Views.UserRecordlist;
import com.webim.im.module.server.SererImpl.view.TemporaryUserinfo;
import com.webim.im.module.dao.custom.Views.ApplyUserListView;
import com.webim.im.module.dao.custom.Views.UserViews;
import com.webim.im.module.dao.views.recordpageView;
import com.webim.im.module.entity.ApplyUser;
import com.webim.im.module.entity.Record;
import com.webim.im.module.entity.User;
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
    Integer getImUserInfo(Integer userCenterID, String username,String avatar);
    // 修改个性签名信息
    User updsign(Integer userid,String  sign);

    // 添加好友
    Object getfriedns(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript);
    User findByTopic(Integer userCenterID);
    // 获取用户列表
    List<UserViews> getuserlist(Integer userid);
    /**
     * @Author zw
     * @Description 删除聊天列表中的信息  刷新下次时该条纪录则不出现在列表中
     * @Date 14:58 2019/8/30
     * @Param
     **/
    Boolean delmsglistInfo(Integer recordid);
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
    Page<UserRecordlist> findUseridRecordCustom(Integer userid, Integer start, Integer limit);

    /**
     * @Author zw
     * @Description    获取SSO ID 获取用户信息 以及和对应的聊天技能
     * @Date 11:22 2019/8/28
     * @Param formuserid 当前登录的用户信息
     **/
    TemporaryUserinfo getSSOIdUserAndRecord(Integer formuserid,Integer topic);

    /**
     * @Author zw
     * @Description 查询根据名称模糊查询消息列表
     * @Date 15:52 2019/8/28
     * @Param
     **/
    List<User> getlistNameUser(Integer userid, String name);
    /**
     * @Author zw
     * @Description 查询根据名称模糊查询好友列表
     * @Date 15:52 2019/8/28
     * @Param
     **/
    List<User> getlistUserNamefriend(Integer userid, String name);
    /**
     * @Author zw
     * @Description  删除好友和聊天
     * @Date 16:33 2019/8/28
     * @Param
     **/
    Boolean delfriendAndRecord(Integer userid, Integer friendid);
    /**
     * @Author zw
     * @Description  好友聊天记录
     * @Date 14:10 2019/8/29
     * @Param
     **/
    recordpageView UserRecordPage(Integer fromid, Integer toid, Integer start, Integer limit, String slursearch, Date starttime);

    /**
     * @Author zw
     * @Description  修改消息状态为已读
     * @Date 11:03 2019/9/2
     * @Param
     **/
    Boolean UseridRecord(Integer userid, Integer friendid);

}
