package com.webim.im.dao.custom.Views;

import lombok.Data;
/**
 * @Author zw
 * @Description 返回 好友申请列表页面所需要的列表
 * @Date 16:20 2019/8/13
 * @Param
 **/
@Data
public class UserViews {
    private  Integer id;
    private  String username;
    private  Integer status;//0 为未申请   1为申请过了 状态

}
