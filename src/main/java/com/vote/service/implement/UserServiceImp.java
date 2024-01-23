package com.vote.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vote.mapper.UserMapper;
import com.vote.pojo.User;
import com.vote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

  @Autowired private UserMapper userMapper;

  @Override
  public boolean log(String username, String password) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", username);
    map.put("password", password);
    List<User> userList = userMapper.selectByMap(map);
    if (userList.size() == 0) {
      return false;
    }
    return true;
  }

  @Override
  public void register(User user) {
    userMapper.insert(user);
  }

  @Override
  public boolean modifyPassword(String username, String passwordBefore, String passwordNew) {
    User user = userMapper.selectById(username);
    if (user.getPassword().equals(passwordBefore)) {
      user.setPassword(passwordNew);
      userMapper.updateById(user);
      return true;
    }
    return false;
  }
}
