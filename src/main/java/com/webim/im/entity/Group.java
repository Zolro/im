package com.webim.im.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
/**
 * @Author zw
 * @Description 好友分组表
 * @Date 14:56 2019/8/6
 * @Param
 **/
@Data
@Entity
@Table(name="im_group")
public class Group {
    @Id
    @GeneratedValue
    private  Integer id;
    @ManyToOne
    @JoinColumn(
            name = "`user`",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private User user;
    private  String name;
    private Date created;
    private Integer groupdelete; // 是否删除  0未删除 1已删除 deleteEnum
    @OneToMany
    @JoinTable(name = "im_friends",joinColumns={@JoinColumn(name="`group`")},inverseJoinColumns={@JoinColumn(name="friend")})
    private List<User> friends;
}
