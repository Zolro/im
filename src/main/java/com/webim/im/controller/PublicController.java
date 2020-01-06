package com.webim.im.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.module.server.UserServer;
import com.webim.im.common.WebSession;
import com.webim.im.common.factorys.ObjectFactory;
import com.webim.im.common.factorys.ReturnMessageFactory;
import com.webim.im.common.pojo.ReturnMessage;
import com.webim.im.common.pojo.UserInfoPojo;
import com.webim.im.utils.ImageUploadUtils;
import com.webim.im.utils.Result;
import com.webim.im.view.ImageView;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
public class PublicController extends WebSession {
  @Autowired UserServer userServer;
  @Autowired ObjectMapper objectMapper;

  @PostMapping("/user/upload")
  public String upload(MultipartFile file) {
    try {
      ImageView view = ImageUploadUtils.upload(file.getOriginalFilename(), file);
      return objectMapper.writeValueAsString(
          Result.of(200, "上传成功", objectMapper.writeValueAsString(view)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      return objectMapper.writeValueAsString(Result.of(0, "失败"));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** @Author zw @Description IM前端向后端获取用户ID 如果对应的用户ID不存在则创建用户 @Date 10:38 2019/8/21 @Param */
  @GetMapping("/getImUserInfo")
  public ReturnMessage getImUserInfo(HttpSession session) {
    UserInfoPojo pojo = getLoginUserInfo(session);
    if (pojo != null) {
      Map<String, Map<String, Object>> map = getUserInfo(pojo.getUserId(), pojo.getToken());
      String name = String.valueOf(map.get("result").get("nickname"));
      String avatar = String.valueOf(map.get("result").get("avatar"));
      Integer topic = Integer.valueOf(String.valueOf(map.get("result").get("id")));
      Integer info = userServer.getImUserInfo(topic, name, avatar);
      log.debug("Im UserInfo:{}", info);
      return ReturnMessageFactory.create(1, null, ObjectFactory.create(pojo,info));
    }
    return ReturnMessageFactory.create(-1, "未登录");
  }

  @Value("${sso.domain}")
  private String ssoDomain;

  @Autowired RestTemplate restTemplate;

  private Map getUserInfo(String userid, String _tb_token_) {
    String url = ssoDomain + "/member/" + userid + "?&_tb_token_=" + _tb_token_;
    return restTemplate.getForObject(url, Map.class);
  }

  private Map getUserInfo(Integer userid, String _tb_token_) {
    String url = ssoDomain + "/member/" + userid + "?&_tb_token_=" + _tb_token_;
    return restTemplate.getForObject(url, Map.class);
  }
}
