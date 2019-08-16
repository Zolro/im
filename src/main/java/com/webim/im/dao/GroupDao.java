package com.webim.im.dao;

import com.webim.im.Server.SererImpl.view.groupfriendsList;
import com.webim.im.dao.custom.GroupDaoCustom;
import com.webim.im.entity.Group;
import com.webim.im.entity.User;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupDao extends JpaRepository<Group,Integer>, GroupDaoCustom {

    List<Group> findByUser(User user);
}
