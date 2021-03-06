package com.webim.im.module.dao.custom;

import java.util.Date;
import java.util.List;

import com.webim.im.module.dao.custom.Views.UserRecordlist;
import com.webim.im.module.dao.views.recordpageView;
import com.webim.im.module.entity.Record;
import com.webim.im.view.Page;

/**
 * @Author zw
 * @Description  好友聊天记录表
 * @Date 14:25 2019/8/13
 * @Param
 **/
public interface RecordDaoCustom {
    /**
     * @Author zw
     * @Description  根据用户id 查询聊天记录 根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
     * @Date 14:26 2019/8/13
     * @Param
     **/
    Page<UserRecordlist> findUseridRecordCustom(Integer userid, Integer start, Integer limit);
    /**
     * @Author zw
     * @Description  获取当前聊天对象的未读取的消息
     * @Date 20:25 2019/8/13
     * @Param
     **/
    List<Record>  findUserRead(Integer formuserid,Integer touserid);
    /**
     * @Author zw
     * @Description  修改聊天消息未已读
     * @Date 11:10 2019/8/15
     * @Param
     **/
    void UseridRecord(Integer formuserid,Integer touserid);
    /**
     * @Author zw
     * @Description   根据用户ID和好友ID 对历史聊天记录进行分页
     * @Date 14:00 2019/8/29
     * @Param
     **/
    recordpageView UserRecordPage(Integer from, Integer to, Integer start, Integer limit, String slursearch, Date starttime);
    /**
     * @Author zw
     * @Description 根据用户ID和好友ID 查询当前聊天记录
     * @Date 14:40 2020/1/14
     * @Param
     **/
    Page pageNowRecord(Integer from,Integer to,Integer start, Integer limit);

}
