package com.webim.im.module.dao.custom.Views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.module.entity.User;

import lombok.Data;

/**
 * @Author zw
 * @Description  用户聊天列表 显示好友聊天已读未读和 未读还有几条信息
 * @Date 9:11 2019/8/15
 * @Param
 **/
@Data
public class UserRecordlist {
    private Integer id;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;
    private Integer type; // 消息类型 文字图片 语言
    private Integer fromId;
    private Integer toId;
    private User to;
    private Boolean state;//是否已经读取过，true代表读取过，false代表未读
    private Boolean issend;// 判断是否是接受者 还是发送者 true 代表发送者 false代表接受者
    private Integer noreadcount;// 未读还有几条信息
    private Boolean existsfriends;
}
