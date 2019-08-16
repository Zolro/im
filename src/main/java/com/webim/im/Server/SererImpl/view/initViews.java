package com.webim.im.Server.SererImpl.view;

import java.util.List;

import com.webim.im.entity.Group;

import lombok.Data;

// 用户登录初始化DTO
@Data
public class initViews {
    String  username;
    Integer id;
    String  avatar;
    String  sign;
    Integer  status;
    List<groupfriendsList> list;
}
