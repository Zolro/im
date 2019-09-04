package com.webim.im.dao.views;

import java.util.Date;
import java.util.List;

import com.webim.im.view.Page;

import lombok.Data;

@Data
public class recordpageView {
    private Date firstdate; // 和用户聊天的第一条信息发送时间
    private Page page;
}
