package com.webim.im.view;

import lombok.Data;

@Data
public class ImageView {

  private String state; // default SUCCESS
  private String name; // eg. 23156121212158163121.jpg
  private String originalName; // eg. image.jpg
  private Long size;
  private String url;
  private String type; // eg. jpg
}
