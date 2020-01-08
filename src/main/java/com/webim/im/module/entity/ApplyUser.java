package com.webim.im.module.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
/** @Author zw @Description 好友申请表 @Date 9:21 2019/8/7 @Param */
@Data
@Entity
@Table(name = "im_applyuser")
public class ApplyUser {
  @Id @GeneratedValue private Integer id;
  /** 申请状态 0待回复 1同意 2不同意 FriendsEnum */
  private Integer status;

  private Date createtime;
  /** 处理者表示 》》》关联用户 */
  @Column(nullable = true)
  private Integer handlderiden;
  /** 发送者 》》》关联用户 */
  @ManyToOne
  @JoinColumn(
      name = "`from`",
      foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
          updatable = false, insertable = false)
  private User from;
  @Column(name = "`from`")
  private Integer fromId;
  @ManyToOne
  @JoinColumn(
      name = "`to`",
      foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
          updatable = false, insertable = false)
  /** 接受者 》》》关联用户 */
  private User to;
  @Column(name = "`to`")
  private Integer toId;
  @ManyToOne
  @JoinColumn(
      name = "`groupiden`",
      foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private Group groupiden; // 发送者的 分组标识 》》》关联分组

  @Column(name = "`type`")
  private Integer type; // 类型： 0好友 1群组 FriendsTypeEnum

  @Column()
  private String postscript; // 附言

  @Column()
  private String reply; // 回复
  @Column()
  private Boolean state;//是否已经读取过，true代表读取过，false代表未读
  @Column()
  private Boolean issend;// 判断是否是接受者 还是发送者 true 代表发送者 false代表接受者
}
