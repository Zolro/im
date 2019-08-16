package com.webim.im.model;

import lombok.Data;

@Data
public class MessageBody {
    private Integer  msgtype; //msgtype 0请求，1 响应 ，2 通知 ，3应答    对应枚举： msgtypeEnum
    private Integer  cmd ;    //cmd 0 绑定 1，心跳 2.上线 3. 下线 4 错误 5 http 请求替换    对应枚举： cmdEnum
    private Integer  sender;  //发送人
    private Integer  receiver;     // 接收人
    private String   content; // 请求数据
    private String  url;
    private Integer  errorcode;
}
