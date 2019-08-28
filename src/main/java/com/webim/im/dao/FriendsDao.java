package com.webim.im.dao;

import com.webim.im.entity.Friends;
import com.webim.im.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsDao extends JpaRepository<Friends,Integer> {
    // 判断申请好友是否已经是好友
    Boolean existsByUserAndFriend(User user, User friend);
    /**
     * @Author zw
     * @Description  删除用户的好友
     * @Date 16:37 2019/8/28
     * @Param
     **/
    Boolean deleteByUserIdAndFriendId(Integer userid,Integer friends);
}
