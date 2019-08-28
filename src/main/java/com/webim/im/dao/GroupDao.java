package com.webim.im.dao;

import com.webim.im.dao.custom.GroupDaoCustom;
import com.webim.im.entity.Group;
import com.webim.im.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupDao extends JpaRepository<Group,Integer>, GroupDaoCustom {

    List<Group> findByUser(User user);
}
