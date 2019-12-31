package com.webim.im.module.dao;

import com.webim.im.module.dao.custom.UserDaoCustom;
import com.webim.im.module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer>, UserDaoCustom {
    /**
     * @Author zw
     * @Description  根据名称查询该用户
     * @Date 11:05 2019/8/7
     * @Param
     **/
    User findByTopic(Integer userCenterID);
}
