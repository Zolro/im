package com.webim.im.dao;

import com.webim.im.entity.Friends;
import com.webim.im.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsDao extends JpaRepository<Friends,Integer> {
    // 判断申请好友是否已经是好友
    Boolean existsByUserAndFriend(User user, User friend);

}
