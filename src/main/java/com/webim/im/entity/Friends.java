package com.webim.im.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @Author zw 好友表
 * @Description //TODO
 * @Date 15:04 2019/8/6
 * @Param
 **/
@Data
@Entity
@Table(name="im_friends")
public class Friends {
    @Id
    @GeneratedValue()
    private  Integer id;
    @ManyToOne
    @JoinColumn(name = "`user`", foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private  User user; // 好友的用户 》》》关联用户
    @ManyToOne
    @JoinColumn(
            name = "friend",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private  User friend; // 好友 》》》关联用户
    @ManyToOne
    @JoinColumn(
            name = "`group`",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private  Group group; // 好友属于对应用户的哪个分组  》》》关联分组
}
