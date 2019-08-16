package com.webim.im.Server;

import java.util.List;

import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.ApplyUser;
import com.webim.im.entity.Record;
import com.webim.im.entity.User;
import com.webim.im.model.MessageBody;
import com.webim.im.view.Page;

import org.springframework.web.multipart.MultipartFile;
// 用户Server 接口
public interface UserServer {
    // 用户登录初始化数据
    Object init(Integer userid);
    // 用户注册
    Object createUser(String username, String password, String sign, MultipartFile avatar);
    // 修改个性签名信息
    User updsign(Integer userid,String  sign);

    // 添加好友
    Object getfriedns(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript);
    User findByTopic(String username);
    // 获取用户列表 测试用方法
    List<UserViews> getuserlist(Integer userid);

    // 根据用户id  查询收到好友申请列表
    List<ApplyUserListView> getapplyfriendlist(Integer userid);
    // 修改好友申请列表
    ApplyUser updApplyUser(Integer applyuserid,Integer state,String reply,Integer groupid);
    /**
     * @Author zw
     * @Description  获取当前聊天对象的未读取的消息 并修改成已读状态
     * @Date 20:33 2019/8/13
     * @Param
     **/
    List<Record> findUserRead(Integer formuserid,Integer touserid);
    /**
     * @Author zw
     * @Description  根据用户id 查询聊天记录 根据是否读取过 排序 未读取优先级高  好友分组 取最新一条数据  有几条未查阅的信息
     * @Date 21:00 2019/8/14
     * @Param
     **/
    Page findUseridRecordCustom(Integer userid, Integer start, Integer limit);

}
