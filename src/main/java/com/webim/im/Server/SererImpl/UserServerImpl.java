package com.webim.im.Server.SererImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Enum.Friend.FriendsEnum;
import com.webim.im.Enum.User.UserAccountEnum;
import com.webim.im.Enum.User.UserEnum;
import com.webim.im.Server.SererImpl.view.GroupfriendsList;
import com.webim.im.Server.SererImpl.view.InitViews;
import com.webim.im.Server.SererImpl.view.TemporaryUserinfo;
import com.webim.im.Server.UserServer;
import com.webim.im.dao.*;
import com.webim.im.dao.custom.Views.ApplyUserListView;
import com.webim.im.dao.custom.Views.UserViews;
import com.webim.im.entity.*;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.utils.Result;
import com.webim.im.view.Page;
import com.xinlianshiye.clouds.sso.common.resource.MemberResource;
import com.xinlianshiye.clouds.sso.common.view.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
    @Value("${sso.domain}")
    private String ssoDomain;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public Object init(Integer userid) {

        User user = userDao.findOne(userid);
        if(user==null){
            return "error";
        }
        InitViews iv=new InitViews();
        //个人信息
        iv.setId(user.getId());
        iv.setUsername(user.getUsername());
        iv.setAvatar(user.getAvatar());
        iv.setSign(user.getSign());
        iv.setStatus(redisReceiver.isUserOnline(user.getId())? UserEnum.ONLINE.ordinal():UserEnum.OFFLINE.ordinal());
        //friend分组信息
        List<Group> groups = groupDao.findByUser(user);
        List<GroupfriendsList> groupfriendsLists=new ArrayList<>();
        groups.forEach(group -> {
            GroupfriendsList gf=new GroupfriendsList();
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
    public Object createUser(Integer userCenterID, String username, String sign, String imageUrl) {
        User a= userDao.findByTopic(userCenterID);
        if(a!=null){
            return "用户名已经存在";
        }
        User user = new User();
        user.setTopic(userCenterID);
        user.setUsername(username);
        user.setSign(sign);
        user.setStatus(UserEnum.ONLINE.ordinal());
        user.setCreated(new Date());
        user.setStatusexist(UserAccountEnum.normal.ordinal());
            user.setAvatar(imageUrl);
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
    }
    @Override
    public User createtoUser(Integer userCenterID, String username, String sign, String imageUrl) {
        User user = new User();
        user.setTopic(userCenterID);
        user.setUsername(username);
        user.setSign(sign);
        user.setStatus(UserEnum.ONLINE.ordinal());
        user.setCreated(new Date());
        user.setStatusexist(UserAccountEnum.normal.ordinal());
            //文件上传到ftp服务器
            user.setAvatar(imageUrl);
            User us=userDao.save(user);
            // 添加用户的时候默认添加分组
            Group gp=new Group();
            gp.setCreated(new Date());
            gp.setGroupdelete(0);
            gp.setName("我的好友");
            gp.setUser(us);
            groupDao.save(gp);
            return user;
    }

    @Override
    public Integer getImUserInfo(Integer userCenterID, String username ,String avatar) {
       User user= userDao.findByTopic(userCenterID);
        if(user==null){
            if(avatar!=""||avatar!=null||!"null".equals(avatar)){
                user=createtoUser(userCenterID,username,"",avatar);
            }else{
                user=createtoUser(userCenterID,username,"","/public/upload/usr/supplier/f1a8b40c6b5ef347fd6e453eb1eae904.jpg");
            }
        }
        return user.getId();
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
    public User findByTopic(Integer userCenterID) {
        return userDao.findByTopic(userCenterID);
    }

    @Override
    public List<UserViews> getuserlist(Integer useriod ) {
        return userDao.getuserlist(useriod);
    }
    @Override
    public   Boolean delmsglistInfo(Integer recordid){
      Record record=   recordDao.findOne(recordid);
        if(record!=null){
            record.setSigndel(true);
        }
        return  true;
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
        // 如果对方也同时申请了好友 需同时也修改对应的值
        ApplyUser ObapplyUser=applyUserDao.findByFromAndTo(applyUser.getTo(),applyUser.getFrom());
        if(ObapplyUser!=null){
            ObapplyUser.setStatus(state);
            applyUserDao.save(ObapplyUser);
        }
         // 给双方在好友表中添加对应的消息
        if(state==1){
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
        }
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

    @Override
    public TemporaryUserinfo getSSOIdUserAndRecord(Integer formuserid,Integer topic) {
        TemporaryUserinfo temporaryUserinfo=new TemporaryUserinfo();
        User user=userDao.findByTopic(topic);
        if(user ==null){
            Member map= getuserinfo(topic);
            user=createtoUser(map.getId(),map.getNickname(),"","/public/upload/usr/supplier/f1a8b40c6b5ef347fd6e453eb1eae904.jpg");
        }else{
            temporaryUserinfo.setList(findUserRead(formuserid, topic));
        }
        temporaryUserinfo.setId(user.getId());
        temporaryUserinfo.setAvatar(user.getAvatar());
        temporaryUserinfo.setSign(user.getSign());
        temporaryUserinfo.setStatus(user.getStatus());
        temporaryUserinfo.setUsername(user.getUsername());
        return  temporaryUserinfo;
    }

    @Override
    public List<User> getlistNameUser(Integer userid, String name) {
        return userDao.getlistUserName(userid,name);
    }

    @Override
    public List<User> getlistUserNamefriend(Integer userid, String name) {
        return userDao.getlistUserNamefriend(userid,name);
    }
    @Override
    public Boolean delfriendAndRecord(Integer userid, Integer friendid) {
        friendsDao.deleteByUserIdAndFriendId(userid,friendid);
        friendsDao.deleteByUserIdAndFriendId(friendid,userid);
        recordDao.deleteByFromIdAndToId(userid,friendid);
        applyUserDao.deleteByFromIdAndToId(userid,friendid);
        applyUserDao.deleteByFromIdAndToId(friendid,userid);
        return  true;
    }

    @Override
    public Page UserRecordPage(Integer fromid, Integer toid, Integer start, Integer limit) {
        return recordDao.UserRecordPage(fromid,toid,start,limit);
    }

    private Member getuserinfo(Integer topic){
        return new MemberResource(ssoDomain).fetchMember(topic.toString(), null);
    }
}
