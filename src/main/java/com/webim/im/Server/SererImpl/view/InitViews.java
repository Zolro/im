package com.webim.im.Server.SererImpl.view;

import java.util.List;

import lombok.Data;

// 用户登录初始化DTO
@Data
public class InitViews {
    String  username;
    Integer id;
    String  avatar;
    String  sign;
    Integer  status;
    List<GroupfriendsList> list;
}
