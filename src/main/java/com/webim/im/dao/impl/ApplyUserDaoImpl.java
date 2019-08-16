package com.webim.im.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.webim.im.dao.BaseRepository;
import com.webim.im.dao.custom.ApplyUserDaoCustom;
import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.QApplyUser;

public class ApplyUserDaoImpl extends BaseRepository implements ApplyUserDaoCustom {
    @Override
    public List<ApplyUserListView> getapplyfriendlist(Integer userid) {
        QApplyUser applyUser=QApplyUser.applyUser;
        List<ApplyUserListView> list= new ArrayList<>();
                queryFactory.select(applyUser.id,applyUser.status,
                applyUser.createtime,applyUser.from.id,applyUser.from.username,applyUser.type,applyUser.postscript)
                .from(applyUser).where(applyUser.to.id.eq(userid)).fetch()
                        .stream().forEach(tuple -> {
                    ApplyUserListView au=new ApplyUserListView();
                    au.setId(tuple.get(applyUser.id));
                    au.setStatus(tuple.get(applyUser.status));
                    au.setCreatetime(tuple.get(applyUser.createtime));
                    au.setFromid(tuple.get(applyUser.from.id));
                    au.setFromusername(tuple.get(applyUser.from.username));
                    au.setPostscript(tuple.get(applyUser.postscript));
                    list.add(au);
                });
        return list;
    }
}
