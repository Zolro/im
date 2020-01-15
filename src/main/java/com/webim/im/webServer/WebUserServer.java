package com.webim.im.webServer;

import com.webim.im.model.MessageBody;

import java.util.Date;

public interface WebUserServer {
    /**
     * 用户登录初始化数据
     */
    MessageBody init(Integer userid);

    /**
     * 修改个性签名信息
     */
    MessageBody updsign(Integer userid, String sign);

    /**
     * @Author zw @Description 根据用户id 显示用户好友聊天列表 根据是否读取过 排序 未读取优先级高 好友分组 取最新一条数据 有几条未查阅的信息 @Date 10:10
     * 2019/8/15 @Param
     */
    MessageBody findUseridRecordCustom(Integer userid, Integer start, Integer limit);

    /**
     * @Author zw @Description 添加好友 @Date 11:01 2019/8/28 @Param
     */
    MessageBody getfriends(
            Integer userid, Integer touserId, Integer groupiden, Integer type, String postscript);

    /**
     * @Author zw @Description 查询根据名称模糊查询消息列表 @Date 15:52 2019/8/28 @Param
     */
    MessageBody getListUserName(Integer userid, String name);

    /**
     * @Author zw @Description 查询根据名称模糊查询好友列表 @Date 15:52 2019/8/28 @Param
     */
    MessageBody getListUserNameFriend(Integer userid, String name);

    /**
     * @Author zw @Description 根据用户拉去对应的用户信息 @Date 11:02 2019/8/28 @Param
     */
    MessageBody findByTopic(String username);

    /**
     * @Author zw @Description 获取用户列表 @Date 11:01 2019/8/28 @Param
     */
    MessageBody getUserList(Integer userid);

    /**
     * @Author zw @Description 获取SSO ID 获取用户信息 以及和对应的聊天技能 @Date 11:18 2019/8/28 @Param
     */
    MessageBody getSSOIdUserAndRecord(Integer userid, Integer topic);

    /**
     * @Author zw @Description 根据用户id 查询收到好友申请列表 @Date 11:01 2019/8/28 @Param
     */
    MessageBody getApplyFriendList(Integer userid);

    /**
     * @Author zw @Description 修改好友申请列表 @Date 11:02 2019/8/28 @Param
     */
    MessageBody updApplyUser(Integer from,Integer applyuserid, Integer state, String reply, Integer groupid);

    /**
     * @Author zw @Description 获取当前聊天对象的未读取的消息 并修改成已读状态 @Date 20:33 2019/8/13 @Param
     */
    MessageBody findUserRead(Integer formuserid, Integer touserid);

    /**
     * @Author zw @Description 封装返回结果给前端 @Date 15:08 2019/8/14 @Param
     */
    MessageBody packagingulrpublic(String url, String content, Integer userid);
    /**
     * @Author zw @Description 获取当前方法的名称; @Date 15:14 2019/8/14 @Param
     */
    String getMethodName();

    /**
     * @Author zw @Description 将对象转换成String @Date 15:09 2019/8/14 @Param
     */
    String convertObejctToString(Object object);

    /**
     * @Author zw @Description 删除好友 @Date 16:39 2019/8/28 @Param
     */
    MessageBody delFriendAndRecord(Integer userid, Integer friendid);

    /**
     * @Author zw @Description 历史好友聊天记录 @Date 14:12 2019/8/29 @Param
     */
    MessageBody UserRecordPage(
            Integer fromid,
            Integer toid,
            Integer start,
            Integer limit,
            String slursearch,
            Date starttime);

    /**
     * @Author zw
     * @Description 当前好友聊天记录
     * @Date 15:42 2020/1/14
     * @Param
     **/
    MessageBody pageNowRecord(
            Integer from,
            Integer to,
            Integer start,
            Integer limit);

    /**
     * @Author zw @Description 关闭聊天信息 @Date 9:54 2019/9/2 @Param
     */
    MessageBody delMsgListInfo(Integer fromid, Integer recordid);

    /**
     * @Author zw @Description 修改信息内容为已读 @Date 11:06 2019/9/2 @Param
     */
    MessageBody UseridRecord(Integer userid, Integer friendid);
}
