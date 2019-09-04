package com.webim.im.dao.views;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.view.Page;

import lombok.Data;

@Data
public class recordpageView {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date firstdate; // 和用户聊天的第一条信息发送时间
    private Page page;
}
