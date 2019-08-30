package com.webim.im.dao;

import java.util.List;

import com.webim.im.dao.custom.ApplyUserDaoCustom;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplyUserDao  extends JpaRepository<ApplyUser,Integer>, ApplyUserDaoCustom {
    /**
     * @Author zw
     * @Description 判断申请好友是否已经申请了该好友
     * @Date 15:31 2019/8/9
     * @Param
     **/

    Boolean existsByFromAndToAndAndStatus(User form, User to,Integer state);

    /**
     * @Author zw
     * @Description  删除好友申请记录
     * @Date 9:56 2019/8/30
     * @Param
     **/
    Integer deleteByFromIdAndToId(Integer fromId,Integer toid);
}
