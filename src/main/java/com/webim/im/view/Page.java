package com.webim.im.view;

import java.util.List;

import lombok.Data;

@Data
public class Page<T > {
  private int totalCount;
  private int totalPage;
  private int currentPage;
  private int start;
  private int limit;
  private int countNoRead;
  private List<T> items;
  private Object object;

  public Page(int totalCount, int totalPage, int currentPage, int start, int limit, List<T> items) {
    super();
    this.totalCount = totalCount;
    this.totalPage = totalPage;
    this.currentPage = currentPage;
    this.start = start;
    this.limit = limit;
    this.items = items;
  }

  public Page(List<T> items, int start, int limit, int totalCount) {
    this.items = items;
    this.start = start;
    this.limit = limit;
    this.totalCount = totalCount;
    if (limit == 0) {
      this.totalPage = 1;
      this.currentPage = 1;
    } else {
      this.totalPage = totalCount / limit + (totalCount % limit > 0 ? 1 : 0);
      this.currentPage = start / limit + 1;
    }
  }

  public Page(List<T> items, int totalCount, Object object) {
    this.items = items;
    this.totalCount = totalCount;
    this.object = object;
  }

  public Page(List<T> items, int start, int limit, int totalCount, int countNoRead) {
    this.items = items;
    this.start = start;
    this.limit = limit;
    this.countNoRead = countNoRead;
    this.totalCount = totalCount;
    if (limit == 0) {
      this.totalPage = 1;
      this.currentPage = 1;
    } else {
      this.totalPage = totalCount / limit + (totalCount % limit > 0 ? 1 : 0);
      this.currentPage = start / limit + 1;
    }
  }

}
