package com.webim.im.dao.custom.Views;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.webim.im.entity.User;

import lombok.Data;

@Data
public class ApplyUserListView {
    private Integer id;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    private Integer fromid;
    private String  fromusername;
    private Integer type;
    private String  postscript;
}
