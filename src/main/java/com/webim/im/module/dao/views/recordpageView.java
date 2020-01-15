package com.webim.im.module.dao.views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.view.Page;

import lombok.Data;

@Data
public class recordpageView {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date firstdate; // 和用户聊天的第一条信息发送时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date enddate; // 和用户聊天的最后一天信息发送时间
    private Page page;
}
