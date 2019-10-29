package com.webim.im.common.pojo;

import lombok.Data;

/**
 * The type Return messages.
 *
 * <p>*
 *
 * @author lijie
 * @date 2019 /10/28
 * @since
 */
@Data
public class ReturnMessage {
  /** The Code. */
  private Integer code;

  /** The Msg. */
  private String msg;

  private Object result;
}
