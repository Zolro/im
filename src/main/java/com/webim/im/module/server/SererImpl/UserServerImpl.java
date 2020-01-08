package com.webim.im.module.server.SererImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.Enum.Friend.FriendsEnum;
import com.webim.im.Enum.User.UserAccountEnum;
import com.webim.im.Enum.User.UserEnum;
import com.webim.im.module.dao.custom.Views.UserRecordlist;
import com.webim.im.module.server.SererImpl.view.GroupfriendsList;
import com.webim.im.module.server.SererImpl.view.InitViews;
import com.webim.im.module.server.SererImpl.view.TemporaryUserinfo;
import com.webim.im.module.server.UserServer;
import com.webim.im.module.dao.*;
import com.webim.im.module.dao.custom.Views.ApplyUserListView;
import com.webim.im.module.dao.custom.Views.UserViews;
import com.webim.im.module.dao.views.recordpageView;
import com.webim.im.module.entity.*;
import com.webim.im.utils.RedisReceiver;
import com.webim.im.utils.Result;
import com.webim.im.view.Page;
import com.xinlianshiye.clouds.sso.common.resource.MemberResource;
import com.xinlianshiye.clouds.sso.facade.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${xwt.domain}")
    private String xwtDomain;
    private Map getUserInfo(Integer ssoid) { //获取鞋网通用户信息
        String url = xwtDomain+ ssoid;
        Map map= restTemplate.getForObject(url, Map.class);
        if(map.get("code").equals(1)){
            Map result= (Map)map.get("result");
            return  result;
        }
        return  null;
    }
    @Override
    public Object init(Integer userid) {

        User user = userDao.findOne(userid);
        if(user==null){
            return "error";
        }
        InitViews iv=new InitViews();
        //个人信息
        iv.setId(user.getId());
        setXwtInfo(user);
        iv.setUsername(user.getUsername());
        iv.setAvatar(user.getAvatar());
        iv.setSign(user.getSign());
        Integer applyNum=applyUserDao.countApplyMsg(userid);
        iv.setChaeckNum(applyNum+recordDao.countByFromIdAndState(userid,false));
        iv.setApplyNum(applyNum);
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
                ur.setSign(us.getSign());
                ur.setTopic(us.getTopic());
                setXwtInfo(ur);
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
            if(avatar!=""&avatar!=null&!"null".equals(avatar)){
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
    public Object getfriends(Integer userid,Integer touserId,Integer groupiden,Integer type,String postscript) {

        ApplyUser au= new ApplyUser();
        User fromUser = userDao.findOne(userid);
        User toUser=  userDao.findOne(touserId);
        if(userid==touserId){
            return Result.of(500,"不能添加自己为好友");
        }
        // 判断申请好友是否已经是好友
        if(friendsDao.existsByUserAndFriend(fromUser, toUser)){
            return Result.of(500,"该好友已经是你好友了");
        }
        //判断申请好友是否已经申请了该好友
        if(applyUserDao.existsByFromAndToAndAndStatus(fromUser, toUser,FriendsEnum.waitapply.ordinal())){
            return Result.of(500,"已经申请了该好友");
        }
        au.setFromId(userid);
        au.setToId(touserId);
        au.setState(true);
        au.setIssend(true);
        Group gp= groupDao.findOne(groupiden);
        au.setGroupiden(gp);
        au.setType(type);
        au.setPostscript(postscript);
        au.setStatus(FriendsEnum.waitapply.ordinal());
        au.setCreatetime(new Date());
        au=applyUserDao.save(au);

        ApplyUser toAu= new ApplyUser();
        toAu.setFromId(touserId);
        toAu.setToId(userid);
        toAu.setState(false);
        toAu.setIssend(false);
        toAu.setGroupiden(gp);
        toAu.setType(type);
        toAu.setPostscript(postscript);
        toAu.setStatus(FriendsEnum.waitapply.ordinal());
        toAu.setCreatetime(new Date());
        applyUserDao.save(toAu);
        redisReceiver.init(touserId);
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
            recordDao.saveAndFlush(record);
            return true;
        }
        return  false;
    }
    @Override
    public List<ApplyUserListView> getapplyfriendlist(Integer userid) {
        List<ApplyUserListView> getapplyfriendlist = applyUserDao.getapplyfriendlist(userid);
        getapplyfriendlist.stream().map(bean->{
            setRead(bean);// 设置未已读
            User to= userDao.findById(bean.getFromid());
            Map map = getUserInfo(to.getTopic());
            if(map!=null){
                bean.setFromusername(map.get("nickname").toString());
                Object object=map.get("avatar");
                if(object!=null){
                    bean.setAvatar(object.toString());
                }
            }
            return bean;
        }).collect(Collectors.toList());
        return getapplyfriendlist;
    }
    @Override
    public ApplyUser updApplyUser(Integer applyuserid,Integer state,String reply,Integer groupid) {
        // 修改好友申请表信息
        ApplyUser applyUser= applyUserDao.findOne(applyuserid);
        applyUser.setStatus(state);
        applyUser.setReply(reply);
        ApplyUser au= applyUserDao.save(applyUser);
        ApplyUser ObapplyUser=applyUserDao.findTopByFromIdAndToIdAndStatusOrderByCreatetimeDesc(applyUser.getToId(),applyUser.getFromId(),0);
        if(ObapplyUser!=null){
            ObapplyUser.setStatus(state);
            ObapplyUser.setReply(reply);
            ObapplyUser.setState(false);
            applyUserDao.save(ObapplyUser);
        }
         // 给双方在好友表中添加对应的消息
        if(state==1){
            Friends friends=new Friends();
            friends.setUser(applyUser.getTo());
            friends.setFriend(applyUser.getFrom());
            Group formgroup=groupDao.findOne(applyUser.getGroupiden().getId());
            friends.setGroup(formgroup);
            friendsDao.save(friends);
            Friends friendsuser=new Friends();
            friendsuser.setUser(applyUser.getFrom());
            friendsuser.setFriend(applyUser.getTo());
            Group togroup=groupDao.findOne(groupid);
            friendsuser.setGroup(togroup);
            friendsDao.save(friendsuser);
        }
        redisReceiver.init(applyUser.getTo().getId());
        return au;
    }

    @Override
    public List<Record> findUserRead(Integer formuserid, Integer touserid) {
        List<Record> list= recordDao.findUserRead(formuserid,touserid);
        recordDao.UseridRecord(formuserid,touserid);
        return list;
    }

    @Override
    public Page<UserRecordlist> findUseridRecordCustom(Integer userid, Integer start, Integer limit) {
        Page<UserRecordlist>  page=recordDao.findUseridRecordCustom(userid,start,limit);
        page.getItems().stream().map(bean->{
            User to= bean.getTo();
            setXwtInfo(to);
            bean.setTo(to);
            return bean;
        }).collect(Collectors.toList());
        return page;
    }

    @Override
    public TemporaryUserinfo getSSOIdUserAndRecord(Integer formuserid,Integer topic) {
        TemporaryUserinfo temporaryUserinfo=new TemporaryUserinfo();
        User user=userDao.findByTopic(topic);
        if(user ==null){
            Member map= getSSOUserInfo(topic);
            user=createtoUser(map.getId(),map.getNickname(),"","/public/upload/usr/supplier/f1a8b40c6b5ef347fd6e453eb1eae904.jpg");
        }else{
            temporaryUserinfo.setList(findUserRead(formuserid, topic));
        }
        temporaryUserinfo.setId(user.getId());
        temporaryUserinfo.setSign(user.getSign());
        temporaryUserinfo.setStatus(user.getStatus());
        temporaryUserinfo.setAvatar(user.getAvatar());
        temporaryUserinfo.setUsername(user.getUsername());
        getRoleInfo(temporaryUserinfo, user);
        return  temporaryUserinfo;
    }
    @Override
    public List<User> getlistNameUser(Integer userid, String name) {
        List<User> list=userDao.getlistUserName(userid,name);
        list.stream().map(bean->{
            setXwtInfo(bean);
            return bean;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<User> getlistUserNamefriend(Integer userid, String name) {
        List<User> list=userDao.getlistUserNamefriend(userid,name);
        list.stream().map(bean->{
            setXwtInfo(bean);
            return bean;
        }).collect(Collectors.toList());
        return list;
    }
    @Override
    public Boolean delfriendAndRecord(Integer userid, Integer friendid) {
        friendsDao.deleteByUserIdAndFriendId(userid,friendid);
        friendsDao.deleteByUserIdAndFriendId(friendid,userid);
        recordDao.deleteByFromIdAndToId(userid,friendid);
        applyUserDao.deleteByFromIdAndToId(userid,friendid);
        applyUserDao.deleteByFromIdAndToId(friendid,userid);
        redisReceiver.init(friendid);
        return  true;
    }

    @Override
    public recordpageView UserRecordPage(Integer fromid, Integer toid, Integer start, Integer limit, String slursearch, Date starttime) {
        return recordDao.UserRecordPage(fromid,toid,start,limit,slursearch,starttime);
    }

    @Override
    public Boolean UseridRecord(Integer userid, Integer friendid) {
        recordDao.UseridRecord ( userid, friendid);
        return  true;
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    private Member getSSOUserInfo(Integer topic){
        return new MemberResource(ssoDomain).fetchMember(topic.toString(), null);
    }

    private void setXwtInfo(User to) { //设置鞋网通用户信息
        Map map = getUserInfo(to.getTopic());
        to.setUsername(map.get("nickname").toString());
        Object avatar= map.get("avatar");
        if(avatar!=null){
            to.setAvatar(avatar.toString());
        }
    }
    private void getRoleInfo(TemporaryUserinfo temporaryUserinfo, User user) {
        Map map = getUserInfo(user.getTopic());
        if(map!=null){
            temporaryUserinfo.setUsername(map.get("nickname").toString());
            Object avatar= map.get("avatar");
            if(avatar!=null){
                temporaryUserinfo.setAvatar(avatar.toString());
            }
        }
    }
    private void setRead(ApplyUserListView bean) {
        if(bean.getState()==false){
            ApplyUser user=applyUserDao.findById(bean.getId());
            user.setState(true);
            applyUserDao.save(user);
        }
    }
}
