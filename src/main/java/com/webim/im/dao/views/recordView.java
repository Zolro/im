package com.webim.im.dao.views;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.entity.User;

import lombok.Data;

/**
 * @Author zw
 * @Description  聊天记录
 * @Date 15:55 2019/9/4
 * @Param
 **/
@Data
public class recordView {
    private  Integer id; // 表ID
    private String content; // 聊天内容
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beforedate; // 聊天发送时间
    @JsonFormat(pattern = " HH:mm:ss", timezone = "GMT+8")
    private Date afterdate; // 聊天发送时间
    private Integer type; // 消息类型 文字图片 语言
    private Integer fromId;
    private Integer toId;
    private Boolean state;//是否已经读取过，true代表读取过，false代表未读
    private Boolean issend;// 判断是否是接受者 还是发送者 true 代表发送者 false代表接受者
    private Boolean signdel;// 标记是否删除  一旦删除在消息列表中 则查不出该条记录
}
