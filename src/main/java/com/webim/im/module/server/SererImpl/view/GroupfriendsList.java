package com.webim.im.module.server.SererImpl.view;

import java.util.List;

import com.webim.im.module.entity.User;

import lombok.Data;

@Data
public class GroupfriendsList {
    private  Integer id;
    private  String name;
    private List<User> list;
}
