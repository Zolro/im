package com.webim.im.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.webim.im.Enum.User.UserAccountEnum;
import com.webim.im.dao.BaseRepository;
import com.webim.im.dao.FriendsDao;
import com.webim.im.dao.UserDao;
import com.webim.im.dao.custom.UserDaoCustom;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.QFriends;
import com.webim.im.entity.QUser;
import com.webim.im.entity.User;

import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoImpl extends BaseRepository implements UserDaoCustom {
    @Autowired
    FriendsDao friendsDao;
    @Autowired
    UserDao userDao;
    @Override
    public List<User> findAllWhoFollwMe(Integer userid) {
        QFriends friends =QFriends.friends;
        return queryFactory.select(friends.user).from(friends)
                .where(friends.friend.id.eq(userid)).where(friends.user.statusexist.eq(UserAccountEnum.normal.ordinal())).fetch();
    }

    @Override
    public List<UserViews> getuserlist(Integer userid) {
        QFriends friends =QFriends.friends;
        QUser user =QUser.user;
       List<Integer> list= queryFactory.select(friends.friend.id).from(friends)
                .where(friends.user.id.eq(userid)).fetch();
       list.add(userid);
        List<User> fetch = queryFactory
                .select(Projections.bean(User.class, user.id, user.username))
                .from(user)
                .where(user.id.notIn(list)).fetch();
        List<UserViews>  userViewsList=new ArrayList<>();

        fetch.forEach(user1 -> {
            //判断用户是否以及申请过了
            UserViews uv=new UserViews();
            uv.setId(user1.getId());
            uv.setUsername(user1.getUsername());
            // 0 为未申请   1为申请过了 状态
            if(friendsDao.existsByUserAndFriend(userDao.findOne(userid),user1)){
                uv.setStatus(1);
            }else{
                uv.setStatus(0);
            }
            userViewsList.add(uv);
        });
        return userViewsList;
    }
}
