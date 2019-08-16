package com.webim.im.dao.custom;

import java.util.List;

import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.User;

public interface UserDaoCustom {
    /**
     * 谁的好友列表中有我
     * @param userid
     * @return
     */
    List<User> findAllWhoFollwMe(Integer userid);
    /**
     * @Author zw   查询的用户列表
     * @Description //TODO  测试用的
     * @Date 16:49 2019/8/8
     * @Param
     **/
     List<UserViews> getuserlist(Integer userid);
}
