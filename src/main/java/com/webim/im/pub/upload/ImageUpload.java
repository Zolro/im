package com.webim.im.pub.upload;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webim.im.entity.User;
import com.webim.im.utils.MD5Util;
import com.webim.im.view.ImageView;


public class ImageUpload {
 // public static final Log LOG = new Log(ImageUpload.class);

  public static void main(String[] args)  {
    try {
      String path = "C:/Users/admin/Desktop/images/tieguangyin.jpg";
      File file = new File(path);
       System.out.println(file.exists());
      ImageView view = upload(User.class, "第一张图片.jpg", file);
      System.out.println(new ObjectMapper().writeValueAsString(view));
    } catch (Exception e) {
    }
  }

  public static <T> ImageView upload(
      Class<T> beanClass, String filename, File file) {
    if (!isImage(filename)) {
     // throw LOG.err("typeErr", "image type is not supported, it must in \"jpg,png,gif,bmp,jpeg\"");
    }
    String suffix = suffix(filename);
    String targetname = MD5Util.getMD5(file);
    String url = OssUtil.upload(beanClass, targetname + suffix, file);
    ImageView view = new ImageView();
    view.setName(targetname + suffix);
    view.setOriginalName(filename);
    view.setSize(file.length());
    view.setState("SUCCESS");
    view.setType(suffix.substring(1));
    view.setUrl(url);
    return view;
  }

  public static <T> ImageView upload2(
      Class<T> beanClass, String filename, File file) {
    String suffix = suffix(filename);
    String targetname = MD5Util.getMD5(file);
    String url = OssUtil.upload(beanClass, targetname + suffix, file);
    ImageView view = new ImageView();
    view.setName(targetname + suffix);
    view.setOriginalName(filename);
    view.setSize(file.length());
    view.setState("SUCCESS");
    view.setType(suffix.substring(1));
    view.setUrl(url);
    return view;
  }

  public static String suffix(String filename) {
    return filename.substring(filename.lastIndexOf("."));
  }

  public static boolean isImage(String fn) {
    fn = fn.toLowerCase();
    return fn.endsWith(".jpg")
        || fn.endsWith(".png")
        || fn.endsWith(".gif")
        || fn.endsWith(".bmp")
        || fn.endsWith(".jpeg");
  }
}
