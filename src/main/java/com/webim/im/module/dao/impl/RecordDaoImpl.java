package com.webim.im.module.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.webim.im.module.dao.BaseRepository;
import com.webim.im.module.dao.custom.RecordDaoCustom;
import com.webim.im.module.dao.custom.Views.UserRecordlist;
import com.webim.im.module.dao.views.recordView;
import com.webim.im.module.dao.views.recordpageView;
import com.webim.im.module.entity.QFriends;
import com.webim.im.module.entity.QRecord;
import com.webim.im.module.entity.Record;
import com.webim.im.module.entity.User;
import com.webim.im.utils.DateTimeUtils;
import com.webim.im.view.Page;

public class RecordDaoImpl extends BaseRepository implements RecordDaoCustom {
    @Override
    public Page<UserRecordlist> findUseridRecordCustom(Integer userid, Integer start, Integer limit) {
        QRecord record = QRecord.record;
        QFriends friends = QFriends.friends;
        List<Integer> list = new ArrayList<>();
        queryFactory.select(record.toId, record.created.max()).from(record).where(record.from.id.eq(userid)).groupBy(record.toId).fetch().forEach(tuple -> {
            List<Integer> con = queryFactory.select(record.id)
                    .from(record)
                    .where(record.toId.eq(tuple.get(record.toId)))
                    .where(record.created.eq(tuple.get(record.created.max())))
                    .groupBy(record.toId)
                    .fetch();
            con.forEach(tuple1 -> {
                list.add(tuple1);
            });
        });
        JPAQuery<Record> recordResult = queryFactory.select(record)
                .from(record).where(record.id.in(list)).where(record.signdel.eq(false)).orderBy(record.created.desc());
        Integer recirdCount = (int) recordResult.fetchCount();
        List<Record> rd = recordResult
                .offset(start).limit(limit).fetch();
        List<UserRecordlist> listuser = new ArrayList<>();
        rd.forEach(record1 -> {
            UserRecordlist userRecordlist = new UserRecordlist();
            userRecordlist.setId(record1.getId());
            userRecordlist.setContent(record1.getContent());
            userRecordlist.setCreated(record1.getCreated());
            userRecordlist.setFromId(record1.getFromId());
            userRecordlist.setIssend(record1.getIssend());
            userRecordlist.setState(record1.getState());
            userRecordlist.setToId(record1.getToId());
            Boolean friendsexists = queryFactory.select(friends.id).from(friends).where(friends.user.id.eq(record1.getFromId())).where(friends.friend.id.eq(record1.getToId())).fetchCount() > 0;
            userRecordlist.setExistsfriends(friendsexists);
            User touser = new User();
            User user = record1.getTo();
            touser.setId(user.getId());
            touser.setUsername(user.getUsername());
            touser.setAvatar(user.getAvatar());
            touser.setTopic(user.getTopic());
            userRecordlist.setTo(touser);
            userRecordlist.setType(record1.getType());
            long num = queryFactory.select(record).from(record).where(record.from.id.eq(record1.getFromId())).where(record.to.id.eq(record1.getToId())).where(record.state.eq(false)).fetchCount();
            Integer count = Integer.valueOf(String.valueOf(num));
            userRecordlist.setNoreadcount(count);
            listuser.add(userRecordlist);
        });

        return new Page(listuser, start, limit, recirdCount);
    }

    @Override
    public List<Record> findUserRead(Integer formuserid, Integer touserid) {
        QRecord record = QRecord.record;
        List<Record> list = new ArrayList<>();
        queryFactory.select(record.id, record.created, record.type, record.state, record.content, record.from.id, record.to.id, record.issend)
                .from(record)
                .where(record.from.id.eq(formuserid))
                .where(record.to.id.eq(touserid))
                .where(record.state.eq(false)).fetch().stream().forEach(tuple -> {
            Record rd = new Record();
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
        QRecord record = QRecord.record;
        queryFactory.update(record).set(record.state, true)
                .where(record.from.id.eq(formuserid))
                .where(record.to.id.eq(touserid))
                .where(record.state.eq(false)).execute();
    }

    @Override
    public recordpageView UserRecordPage(Integer fromid, Integer toid, Integer start, Integer limit, String slursearch, Date starttime) {
        QRecord record = QRecord.record;
        List<Date> listdate=queryFactory.select(record.created).from(record).where(record.from.id.eq(fromid)).where(record.to.id.eq(toid)).orderBy(record.created.asc()).fetch();
        Date first =null;
        Date end =null;
        if(listdate.size()>0){
            first =listdate.get(0);
            end =listdate.get(listdate.size()-1);
            end=DateTimeUtils.getTimesmorning(end);
        }
        JPAQuery<Integer> query=
                queryFactory.select(record.id).from(record)
                .where(record.fromId.eq(fromid))
                .where(record.toId.eq(toid));
        if(slursearch!=null&&!slursearch.equals("")){
            query.where(record.content.contains(slursearch));
            if(starttime!=null){
                query.where(record.created.gt(starttime));
            }
        }else{
            if(starttime!=null){
                query.where(record.created.gt(starttime));
            }
            if(starttime==null){
                query.where(record.created.gt(end));
            }
        }
        List<Integer> listrecordId =query.fetch();
        recordpageView rpageView = new recordpageView();
        rpageView.setFirstdate(first);
        rpageView.setEnddate(end);
        List<recordView> list = new ArrayList<>();
        if(start==null&&!"".equals(start)){
            start=(int)Math.ceil(listrecordId.size() / limit)*limit;
        }
        queryFactory.select(record.id, record.created, record.type, record.state, record.content, record.from.id, record.to.id, record.issend)
                .from(record)
                .where(record.id.in(listrecordId)).orderBy(record.created.asc())
                .offset(start).limit(limit).fetch().forEach(tuple -> {
            recordView rd = new recordView();
            rd.setId(tuple.get(record.id));
            rd.setBeforedate(tuple.get(record.created));
            rd.setAfterdate(tuple.get(record.created));
            rd.setIssend(tuple.get(record.issend));
            rd.setType(tuple.get(record.type));
            rd.setState(tuple.get(record.state));
            rd.setContent(tuple.get(record.content));
            rd.setFromId(tuple.get(record.from.id));
            rd.setToId(tuple.get(record.to.id));
            list.add(rd);
        });
        rpageView.setPage(new Page(list, start, limit, listrecordId.size()));
        return rpageView;
    }

    @Override
    public Page pageNowRecord(Integer from, Integer to, Integer start, Integer limit) {
        QRecord record = QRecord.record;
        List<recordView> list = new ArrayList<>();
        Integer count=new Long(queryFactory.selectFrom(record).where(record.fromId.eq(from)).where(record.toId.eq(to)).fetchCount()).intValue();
        JPAQuery<Tuple> query= queryFactory.select(record.id, record.created, record.type, record.state, record.content, record.from.id, record.to.id, record.issend)
                .from(record)
                .where(record.fromId.eq(from))
                .where(record.toId.eq(to))
                .orderBy(record.created.asc());
        if(count>0){
            if(start==null){
                start=count-3;
            }
            query.offset(start).limit(limit).fetch().forEach(tuple -> {
                recordView rd = new recordView();
                rd.setId(tuple.get(record.id));
                rd.setBeforedate(tuple.get(record.created));
                rd.setAfterdate(tuple.get(record.created));
                rd.setIssend(tuple.get(record.issend));
                rd.setType(tuple.get(record.type));
                rd.setState(tuple.get(record.state));
                rd.setContent(tuple.get(record.content));
                rd.setFromId(tuple.get(record.from.id));
                rd.setToId(tuple.get(record.to.id));
                list.add(rd);
            });
        }
        return new Page(list, start, limit, count);
    }

}
