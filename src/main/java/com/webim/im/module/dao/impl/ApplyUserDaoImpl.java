package com.webim.im.module.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.webim.im.module.dao.BaseRepository;
import com.webim.im.module.dao.custom.ApplyUserDaoCustom;
import com.webim.im.module.dao.custom.Views.ApplyUserListView;
import com.webim.im.module.entity.QApplyUser;

public class ApplyUserDaoImpl extends BaseRepository implements ApplyUserDaoCustom {
    @Override
    public List<ApplyUserListView> getapplyfriendlist(Integer userid) {
        QApplyUser applyUser=QApplyUser.applyUser;
        List<ApplyUserListView> list= new ArrayList<>();
                queryFactory.select(applyUser.id,applyUser.status,
                applyUser.createtime,applyUser.from.id,applyUser.state,applyUser.from.username,applyUser.from.avatar,applyUser.type,applyUser.postscript)
                .from(applyUser).where(applyUser.fromId.eq(userid)).orderBy(applyUser.createtime.desc()).fetch()
                        .stream().forEach(tuple -> {
                    ApplyUserListView au=new ApplyUserListView();
                    au.setId(tuple.get(applyUser.id));
                    au.setState(tuple.get(applyUser.state));
                    au.setStatus(tuple.get(applyUser.status));
                    au.setAvatar(tuple.get(applyUser.from.avatar));
                    au.setCreatetime(tuple.get(applyUser.createtime));
                    au.setFromid(tuple.get(applyUser.from.id));
                    au.setFromusername(tuple.get(applyUser.from.username));
                    au.setPostscript(tuple.get(applyUser.postscript));
                    list.add(au);
                });
        return list;
    }

    @Override
    public Integer countApplyMsg(Integer id) {
        QApplyUser bo=QApplyUser.applyUser;
        return (int)queryFactory.selectFrom(bo).where(bo.from.id.eq(id)).where(bo.state.eq(false)).fetchCount();
    }
}
