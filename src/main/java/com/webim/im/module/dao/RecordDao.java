package com.webim.im.module.dao;

import com.webim.im.module.dao.custom.RecordDaoCustom;
import com.webim.im.module.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordDao extends JpaRepository<Record,Integer>, RecordDaoCustom {
        /**
         * @Author zw
         * @Description  删除聊天记录
         * @Date 9:56 2019/8/30
         * @Param
         **/
        Integer deleteByFromIdAndToId(Integer fromId,Integer toid);
        /**
         * @Author zw
         * @Description 根据接收者 和未读取 查看当前用户未读取的消息总条数
         * @Date 10:36 2020/1/8
         * @Param
         **/
        Integer countByFromIdAndState(Integer toId,Boolean state);

}
