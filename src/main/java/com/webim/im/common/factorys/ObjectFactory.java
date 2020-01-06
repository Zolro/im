package com.webim.im.common.factorys;

import com.webim.im.common.pojo.UserInfoPojo;

/**
 * The type Object factory.
 *
 * <p>*
 *
 * @author lijie
 * @date 2019 /10/29
 * @since
 */
public class ObjectFactory {

  /**
   * Create
   *
   * @author lijie
   * @date 2019 /10/29
   * @since user info pojo.
   * @param id the id
   * @return the user info pojo
   */
  public static UserInfoPojo create(Integer id) {
    UserInfoPojo pojo = new UserInfoPojo();
    pojo.setId(id);
    return pojo;
  }
  public static UserInfoPojo create(UserInfoPojo pojo,Integer id) {
    UserInfoPojo pojo1= new UserInfoPojo();
    if(pojo==null){
      pojo = new UserInfoPojo();
    }
    pojo.setId(id);
    pojo1.setToken(pojo.getToken());
    pojo1.setUserId(pojo.getUserId());
    pojo1.setId(pojo.getId());
    return pojo1;
  }
}
