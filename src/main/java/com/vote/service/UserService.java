package com.vote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vote.pojo.User;

public interface UserService extends IService<User> {
  boolean log(String username, String password);

  void register(User user);

  boolean modifyPassword(String username, String passwordBefore, String passwordNew);
}
