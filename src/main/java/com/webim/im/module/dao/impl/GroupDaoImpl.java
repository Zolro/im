package com.webim.im.module.dao.impl;

import java.util.*;

import com.webim.im.module.entity.QFriends;
import com.webim.im.module.entity.QGroup;
import com.webim.im.module.entity.QUser;
import com.webim.im.module.server.SererImpl.view.GroupfriendsList;
import com.webim.im.module.dao.BaseRepository;
import com.webim.im.module.dao.custom.GroupDaoCustom;
import com.webim.im.module.entity.User;

public class GroupDaoImpl extends BaseRepository implements GroupDaoCustom {

    @Override
    public List<GroupfriendsList> findgroupfriendsList(Integer userid) {
        QFriends friends = QFriends.friends;
        QUser user = QUser.user;
        QGroup group = QGroup.group;

        Map<Integer, GroupfriendsList> map = new HashMap<>();

        queryFactory
                .select(
                        friends.group.id,
                        friends.group.name,
                        friends.friend.id,
                        friends.friend.username
                )
                .from(friends).rightJoin(friends.group)
//                .on(friends.group.id.eq(group.id))
                .where(friends.group.user.id.eq(userid))
//                .where(friends.group.id.eq(group.id))
                .fetch().stream().forEach((tuple) -> {
                    Integer groupId = tuple.get(friends.group.id);
                    String groupName = tuple.get(friends.group.name);
                    Integer userId = tuple.get(friends.friend.id);
                    String username = tuple.get(friends.friend.username);
                    GroupfriendsList group2;
                        if (map.containsKey(groupId)) {
                        group2 = map.get(groupId);
                    } else {
                        group2 = new GroupfriendsList();
                        group2.setId(groupId);
                        group2.setName(groupName);
                        group2.setList(new ArrayList<>());
                        map.put(groupId, group2);
                    }
                    User user2 = new User();
                    user2.setId(userId);
                    user2.setUsername(username);
                    group2.getList().add(user2);
        });
        return new ArrayList<>(map.values());
    }
}
