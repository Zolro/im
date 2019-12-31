package com.webim.im.module.dao.custom;

import java.util.List;
import com.webim.im.module.dao.custom.Views.ApplyUserListView;
public interface ApplyUserDaoCustom {
    /**
     * @Author zw
     * @Description 根据用户id  查询收到好友申请列表
     * @Date 13:56 2019/8/9
     * @Param
     **/
    List<ApplyUserListView> getapplyfriendlist(Integer userid);
}
