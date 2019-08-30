package com.webim.im.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

// 用户聊天记录表
@Data
@Entity
@Table(name="im_record")
public class Record {
    @Id
    @GeneratedValue
    private  Integer id; // 表ID
    private String content; // 聊天内容
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created; // 聊天发送时间
    private Integer type; // 消息类型 文字图片 语言
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "from_user_id",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT),
            updatable = false, insertable = false)
    private User from;// 消息拥有者 》》》关联用户
    @Column(name = "from_user_id")
    private Integer fromId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "to_user_id",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT),
            updatable = false, insertable = false)
    private User to;// 消息拥有的另一个人 》》》关联用户
    @Column(name = "to_user_id")
    private Integer toId;
    private Boolean state;//是否已经读取过，true代表读取过，false代表未读
    private Boolean issend;// 判断是否是接受者 还是发送者 true 代表发送者 false代表接受者
    private Boolean signdel;// 标记是否删除  一旦删除在消息列表中 则查不出该条记录
}
