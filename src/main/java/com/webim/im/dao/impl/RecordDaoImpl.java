package com.webim.im.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.webim.im.dao.BaseRepository;
import com.webim.im.dao.custom.RecordDaoCustom;
import com.webim.im.dao.custom.Views.UserRecordlist;
import com.webim.im.entity.QRecord;
import com.webim.im.entity.Record;
import com.webim.im.entity.User;
import com.webim.im.view.Page;

public class RecordDaoImpl extends BaseRepository implements RecordDaoCustom {
    @Override
    public Page findUseridRecordCustom(Integer userid,Integer start,Integer limit) {
        QRecord record=QRecord.record;
        List<Integer> list = queryFactory.select(record.id)
                .from(record)
                .where(record.from.id.eq(userid))
                .groupBy(record.from,record.to).fetch();
        Integer recirdCount=list.size();
        List<Record> rd=queryFactory.select(record)
                .from(record).where(record.id.in(list))
                .orderBy(record.state.asc(), record.created.desc()).offset(start).limit(limit).fetch();
        List<UserRecordlist> listuser=new ArrayList<>();
        rd.forEach(record1 -> {
            UserRecordlist userRecordlist=new UserRecordlist();
            userRecordlist.setId(record1.getId());
            userRecordlist.setContent(record1.getContent());
            userRecordlist.setCreated(record1.getCreated());
            userRecordlist.setFromId(record1.getFromId());
            userRecordlist.setIssend(record1.getIssend());
            userRecordlist.setState(record1.getState());
            userRecordlist.setToId(record1.getToId());
            User touser=new User();
            User user=record1.getTo();
            touser.setId( user.getId());
            touser.setUsername( user.getUsername());
            touser.setAvatar( user.getAvatar());
            userRecordlist.setTo(touser);
            userRecordlist.setType(record1.getType());
            long num= queryFactory.select(record).from(record).where(record.to.id.eq(record1.getToId())).where(record.state.eq(false)).fetchCount();
            Integer count=Integer.valueOf(String.valueOf(num));
            userRecordlist.setNoreadcount(count);
            listuser.add(userRecordlist);
        });

        return new Page(listuser,start,limit,recirdCount);
    }

    @Override
    public List<Record> findUserRead(Integer formuserid, Integer touserid) {
        QRecord record=QRecord.record;
        List<Record> list=new ArrayList<>();
          queryFactory.select(record.id,record.created,record.type,record.state,record.content,record.from.id,record.to.id,record.issend)
                .from(record)
                .where(record.from.id.eq(formuserid))
                .where(record.to.id.eq(touserid))
                .where(record.state.eq(false)).fetch().stream().forEach(tuple -> {
                Record rd =new Record();
                rd.setId(tuple.get(record.id));
              rd.setCreated(tuple.get(record.created));
              rd.setIssend(tuple.get(record.issend));
              rd.setType(tuple.get(record.type));
              rd.setState(tuple.get(record.state));
              rd.setContent(tuple.get(record.content));
              rd.setFromId(tuple.get(record.from.id));
              rd.setToId(tuple.get(record.to.id));
              list.add(rd);
          });
        return list;
    }

    @Override
    public void UseridRecord(Integer formuserid, Integer touserid) {
        QRecord record=QRecord.record;
        queryFactory.update(record).set(record.state, true)
                .where(record.from.id.eq(formuserid))
                .where(record.to.id.eq(touserid))
                .where(record.state.eq(false)).execute();
    }
}
