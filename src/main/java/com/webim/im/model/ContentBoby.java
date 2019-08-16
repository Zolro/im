package com.webim.im.model;

import java.util.Date;

import lombok.Data;

@Data
public class ContentBoby {
    private  String  content;  //内容
    private Integer type; //发送内容的类型   0 文字 1 文件
}
