package com.webim.im.dao.custom;

import java.util.List;

import com.webim.im.Server.SererImpl.view.GroupfriendsList;

/**
 * @Author zw
 * @Description  分组定制
 * @Date 11:16 2019/8/7
 * @Param
 **/
public interface GroupDaoCustom {
    /**
     * @Author zw
     * @Description      根据用户id  获取好友组和列表集合
     * @Date 11:17 2019/8/7
     * @Param
     **/

    List<GroupfriendsList> findgroupfriendsList(Integer userid);

}
