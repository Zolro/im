package com.webim.im.common.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * The type User info pojo.
 *
 * <p>*
 *
 * @author lijie
 * @date 2019 /10/23
 * @since
 */
@Data
public class UserInfoPojo {
  /** The Id. */
  private Integer id;
  /** The User id. */
  @JsonIgnore private Integer userId;

  /** The Token. */
  @JsonIgnore private String token;
}
