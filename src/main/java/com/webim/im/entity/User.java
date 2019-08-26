package com.webim.im.entity;

import lombok.Data;


import javax.persistence.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

// 用户账号表
@Data
@Entity(name = "im_user")
@Table(name = "im_user")
public class User {
    @Id
    @GeneratedValue
    private Integer id; // 用户id
    private Integer topic;   // 用户中心的用户ID
    private String username;  //用户昵称
    private String sign;      //用户个性签名
    private Integer status;    // 用户状态  在线 下载  枚举类 UserEnum
    private String avatar;    //用户头像
    private Date created;     // 用户创建时间
    @Column(nullable = true)
    private Date logintime;   // 用户登录时间/退出时间
//    @Column(name = "`password`")
//    private String password;  //用户密码
    private Integer statusexist; // 账户状态  （0.删除，1.停用，2.正常） 枚举类 UserAccountEnum
}
