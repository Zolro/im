package com.webim.im.module.dao;

import com.webim.im.module.dao.custom.ApplyUserDaoCustom;
import com.webim.im.module.entity.ApplyUser;
import com.webim.im.module.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

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

    /**
     * @Author zw
     * @Description 根据用户ID和好友ID 查询申请记录
     * @Date 14:31 2019/8/30
     * @Param
     **/
    ApplyUser findByFromAndTo(User from,User to);
}
