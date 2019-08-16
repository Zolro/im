package com.webim.im.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;
/**
 * @Author zw
 * @Description  好友申请表
 * @Date 9:21 2019/8/7
 * @Param
 **/
@Data
@Entity
@Table(name="im_applyuser")
public class ApplyUser {
    @Id
    @GeneratedValue
    private  Integer id;
    private Integer status;               // 申请状态 0待回复  1同意 2不同意  FriendsEnum
    private Date createtime;
    @Column(nullable = true)
    private Integer handlderiden;         // 处理者表示     》》》关联用户
    @ManyToOne
    @JoinColumn(
            name = "`from`",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private User from;                 // 发送者         》》》关联用户
    @ManyToOne
    @JoinColumn(
            name = "`to`",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private User to;                   // 接受者         》》》关联用户
    @ManyToOne
    @JoinColumn(
            name = "`groupiden`",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private Group groupiden;            // 发送者的 分组标识 》》》关联分组
    @Column(name = "`type`")
    private Integer type;                 // 类型： 0好友 1群组 FriendsTypeEnum
    @Column(nullable = true)
    private String  postscript;           // 附言
    @Column(nullable = true)
    private String reply;               //回复
}
