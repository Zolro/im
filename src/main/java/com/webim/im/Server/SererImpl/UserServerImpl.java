package com.webim.im.Server.SererImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Enum.Friend.FriendsEnum;
import com.webim.im.Enum.User.UserAccountEnum;
import com.webim.im.Enum.User.UserEnum;
import com.webim.im.Server.SererImpl.view.groupfriendsList;
import com.webim.im.Server.SererImpl.view.initViews;
import com.webim.im.Server.UserServer;
import com.webim.im.dao.*;
import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.*;
import com.webim.im.utils.ImageUploadUtils;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.utils.Result;
import com.webim.im.view.ImageView;
import com.webim.im.view.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import javax.persistence.EntityManager;

@Service
@Transactional
public class UserServerImpl implements UserServer {
    @Autowired
    UserDao userDao;
    @Autowired
    FriendsDao friendsDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    RecordDao recordDao;
    @Autowired
    ApplyUserDao applyUserDao;
    @Autowired
    RedisReceiver redisReceiver;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private EntityManager entityManager;
    @Override
    public Object init(Integer userid) {

        User user = userDao.findOne(userid);
        if(user==null){
            return "error";
        }
        initViews iv=new initViews();
        //个人信息
        iv.setId(user.getId());
        iv.setUsername(user.getUsername());
        iv.setAvatar(user.getAvatar());
        iv.setSign(user.getSign());
        iv.setStatus(redisReceiver.isUserOnline(user.getId())? UserEnum.ONLINE.ordinal():UserEnum.OFFLINE.ordinal());
        //friend分组信息
        List<Group> groups = groupDao.findByUser(user);
        List<groupfriendsList> groupfriendsLists=new ArrayList<>();
        groups.forEach(group -> {
            groupfriendsList gf=new groupfriendsList();
            gf.setId(group.getId());
            gf.setName(group.getName());
            List<User> users= group.getFriends();
            List<User> userList=new ArrayList<>();
            users.forEach(us->{
                User ur=new User();
                ur.setId(us.getId());
                ur.setUsername(us.getUsername());
                ur.setAvatar(us.getAvatar());
                if(redisReceiver.isUserOnline(us.getId())){
                    ur.setStatus(UserEnum.ONLINE.ordinal());
                }else{
                    ur.setStatus(UserEnum.OFFLINE.ordinal());
                }
                ur.setSign(ur.getSign());
                userList.add(ur);
            });
            gf.setList(userList);
            groupfriendsLists.add(gf);
        });
        iv.setList(groupfriendsLists);
        try {
            return iv;
        }catch (Exception e){
        }
        return null;
    }

    @Override
    public Object createUser(String username, String password, String sign, MultipartFile avatar) {
        User a= userDao.findByTopic(username);
        if(a!=null){
            return "用户名已经存在";
        }

        User user = new User();
        user.setTopic(username);
        user.setUsername(username);
        user.setSign(sign);
        user.setStatus(UserEnum.ONLINE.ordinal());
        user.setCreated(new Date());
        user.setPassword(password);
        user.setStatusexist(UserAccountEnum.normal.ordinal());
        try {
            //文件上传到ftp服务器
            String name=avatar.getOriginalFilename();
            ImageView imageView= ImageUploadUtils.upload(name,avatar);
            user.setAvatar("http://shoestp.oss-us-west-1.aliyuncs.com"+imageView.getUrl());
            User us=userDao.save(user);
            // 添加用户的时候默认添加分组
            Group gp=new Group();
            gp.setCreated(new Date());
            gp.setGroupdelete(0);
            gp.setName("我的好友");
            gp.setUser(us);
            groupDao.save(gp);
            Map<String, Object> data = new HashMap();
            data.put("code", 1);
            data.put("msg", "注册成功");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    public User updsign(Integer userid, String sign) {
        User user = userDao.findOne(userid);
        user.setSign(sign);
        redisReceiver.sendToFollwMe(user.getId(), Result.of()
                .put("type","updateSign")
                .put("sign",sign)
                .put("userid",user.getId()));
        return userDao.save(user);
    }

    @Override
    public Object getfriedns(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript) {
        ApplyUser au= new ApplyUser();
        User fromuser = userDao.findOne(userid);
        au.setFrom(fromuser);
        User tous=  userDao.findOne(touserId);
        // 判断申请好友是否已经是好友
        if(friendsDao.existsByUserAndFriend(fromuser, tous)){
            return Result.of(500,"该好友已经是你好友了");
        }
        //判断申请好友是否已经申请了该好友
        if(applyUserDao.existsByFromAndToAndAndStatus(fromuser, tous,FriendsEnum.waitapply.ordinal())){
            return Result.of(500,"已经申请了该好友");
        }
        tous.setId(touserId);
        au.setTo(tous);
        Group gp= groupDao.findOne(groupiden);
        au.setGroupiden(gp);
        au.setType(type);
        au.setPostscript(postscript);
        au.setStatus(FriendsEnum.waitapply.ordinal());
        au.setCreatetime(new Date());
        applyUserDao.save(au);
        return Result.of(200,"申请成功");
    }

    @Override
    public User findByTopic(String username) {
        return userDao.findByTopic(username);
    }

    @Override
    public List<UserViews> getuserlist(Integer useriod ) {
        return userDao.getuserlist(useriod);
    }

    @Override
    public List<ApplyUserListView> getapplyfriendlist(Integer userid) {
        List<ApplyUserListView> getapplyfriendlist = applyUserDao.getapplyfriendlist(userid);
        return getapplyfriendlist;
    }

    @Override
    public ApplyUser updApplyUser(Integer applyuserid,Integer state,String reply,Integer groupid) {
        // 修改好友申请表信息
        ApplyUser applyUser= applyUserDao.findOne(applyuserid);
        applyUser.setStatus(state);
        applyUser.setReply(reply);
         ApplyUser au= applyUserDao.save(applyUser);
         // 给双方在好友表中添加对应的消息
        Friends friends=new Friends();
        friends.setUser(applyUser.getFrom());
        friends.setFriend(applyUser.getTo());
        Group formgroup=groupDao.findOne(applyUser.getGroupiden().getId());
        friends.setGroup(formgroup);
        friendsDao.save(friends);
        Friends friendsuser=new Friends();
        friendsuser.setUser(applyUser.getTo());
        friendsuser.setFriend(applyUser.getFrom());
        Group togroup=groupDao.findOne(groupid);
        friendsuser.setGroup(togroup);
        friendsDao.save(friendsuser);
        return au;
    }

    @Override
    public List<Record> findUserRead(Integer formuserid, Integer touserid) {
        List<Record> list= recordDao.findUserRead(formuserid,touserid);
        recordDao.UseridRecord(formuserid,touserid);
        return list;
    }

    @Override
    public Page findUseridRecordCustom(Integer userid, Integer start, Integer limit) {
        return recordDao.findUseridRecordCustom(userid,start,limit);
    }
}
