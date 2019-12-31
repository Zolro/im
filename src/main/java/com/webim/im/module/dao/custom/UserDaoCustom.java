package com.webim.im.module.dao.custom;

import java.util.List;

import com.webim.im.module.dao.custom.Views.UserViews;
import com.webim.im.module.entity.User;

public interface UserDaoCustom {
    /**
     * 谁的好友列表中有我
     * @param userid
     * @return
     */
    List<User> findAllWhoFollwMe(Integer userid);
    /**
     * @Author zw   查询的用户列表
     * @Description
     * @Date 16:49 2019/8/8
     * @Param
     **/
     List<UserViews> getuserlist(Integer userid);

     /**
      * @Author zw
      * @Description  模糊查询好友信息 消息列表
      * @Date 16:04 2019/8/28
      * @Param
      **/
     List<User> getlistUserName(Integer userid,String name);

    /**
     * @Author zw
     * @Description  模糊查询好友信息 好友列表
     * @Date 16:04 2019/8/28
     * @Param
     **/
    List<User> getlistUserNamefriend(Integer userid, String name);
}
