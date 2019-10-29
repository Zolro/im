package com.webim.im.common.factorys;

import com.webim.im.common.pojo.ReturnMessage;

/**
 * The type Return message factory.
 *
 * <p>*
 *
 * @author lijie
 * @date 2019 /10/28
 * @since
 */
public class ReturnMessageFactory {
  public static ReturnMessage create(Integer code, String msg, Object o) {
    ReturnMessage message = new ReturnMessage();
    message.setCode(code);
    message.setMsg(msg);
    message.setResult(o);
    return message;
  }

  public static ReturnMessage create(Integer code, String msg) {
    return create(code, msg, null);
  }
}
