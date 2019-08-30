package com.webim.im.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.types.Projections;
import com.webim.im.Enum.User.UserAccountEnum;
import com.webim.im.dao.BaseRepository;
import com.webim.im.dao.FriendsDao;
import com.webim.im.dao.UserDao;
import com.webim.im.dao.custom.UserDaoCustom;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.QFriends;
import com.webim.im.entity.QRecord;
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

    @Override
    public List<User> getlistUserName(Integer userid, String name) {
        QRecord record=QRecord.record;
        QUser user=QUser.user;
        List<Integer> list= queryFactory.select(record.toId).from(record).where(record.signdel.eq(false)).where(record.from.id.eq(userid)).groupBy(record.toId).fetch();
        List<User> userList=new ArrayList<>();
         queryFactory.select(user.id,user.username,user.avatar,user.sign)
                 .from(user).where(user.id.in(list))
                 .where(user.username.like(name)).fetch().stream().forEach(tuple -> {
                     User user1=new User();
             user1.setId(tuple.get(user.id));
             user1.setAvatar(tuple.get(user.avatar));
             user1.setUsername(tuple.get(user.username));
             user1.setSign(tuple.get(user.sign));
             userList.add(user1);
         });
        return userList;
    }
    @Override
    public List<User> getlistUserNamefriend(Integer userid, String name) {
        QRecord record=QRecord.record;
        QFriends friends=QFriends.friends;
        QUser user=QUser.user;
        List<Integer> list= queryFactory.select(friends.friend.id).from(friends).where(record.signdel.eq(false)).where(friends.user.id.eq(userid)).groupBy(friends.friend.id).fetch();
        List<User> userList=new ArrayList<>();
        queryFactory.select(user.id,user.username,user.avatar,user.sign)
                .from(user).where(user.id.in(list))
                .where(user.username.like(name)).fetch().stream().forEach(tuple -> {
            User user1=new User();
            user1.setId(tuple.get(user.id));
            user1.setAvatar(tuple.get(user.avatar));
            user1.setUsername(tuple.get(user.username));
            user1.setSign(tuple.get(user.sign));
            userList.add(user1);
        });
        return userList;
    }
}
