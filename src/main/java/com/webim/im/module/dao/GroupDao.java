package com.webim.im.module.dao;

import com.webim.im.module.dao.custom.GroupDaoCustom;
import com.webim.im.module.entity.Group;
import com.webim.im.module.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDao extends JpaRepository<Group,Integer>, GroupDaoCustom {

    List<Group> findByUser(User user);
}
