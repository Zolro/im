package com.webim.im.dao.custom.Views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.entity.User;

import lombok.Data;

@Data
public class ApplyUserListView {
    private Integer id; // 好友申请表ID
    private Integer status; // 好友申请状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime; // 创建好友申请表的创建时间
    private Integer fromid; // 用户ID
    private String  fromusername; // 用户名称
    private Integer type; //类型
    private String  postscript; // 附言
    private String  avatar;// 头像
}
