package com.webim.im.Server.SererImpl.view;

import java.util.List;

import com.webim.im.entity.User;

import lombok.Data;

@Data
public class GroupfriendsList {
    private  Integer id;
    private  String name;
    private List<User> list;
}
