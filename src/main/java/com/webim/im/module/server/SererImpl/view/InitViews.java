package com.webim.im.module.server.SererImpl.view;

import java.util.List;

import lombok.Data;

// 用户登录初始化DTO
@Data
public class InitViews {
    String  username; // 用户名称
    Integer id;         // 用户id
    String  avatar;     // 用户图片
    String  sign;       // 用户个性签名
    Integer  status;    // 状态
    List<GroupfriendsList> list; //好友分组和好友列表
    Integer chaeckNum; // 未查看的消息总数
    Integer applyNum;  // 申请消息总数
}
