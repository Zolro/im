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
}
