package com.webim.im.view;

import lombok.Data;

@Data
public class ImageResultView {

    private Integer code; //  返回成功失败  0表示成功 其他表示失败
    private String msg; // 失败信息（失败原因）
    private ImageData data;
    @Data
    public class ImageData{
        private  String src;
    }
}
