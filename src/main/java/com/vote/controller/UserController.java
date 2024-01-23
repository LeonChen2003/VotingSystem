package com.vote.controller;

import com.vote.pojo.Result;
import com.vote.pojo.User;
import com.vote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
@CrossOrigin(origins = "*", maxAge = 60)
public class UserController {
  @Autowired private UserService userService;

  @GetMapping
  public Result log(String username, String password) {
    if (userService.log(username, password)) {
      return Result.success();
    }
    return Result.error("账户有误");
  }

  @PostMapping
  public Result register(@RequestBody User user) {
    userService.register(user);
    return Result.success();
  }

  /**
   * 修改密码
   */
  @GetMapping("/modifyPassword")
  public Result modifyPassword(String username, String passwordBefore, String passwordNew){
    if (userService.modifyPassword(username, passwordBefore, passwordNew)){
      return Result.success();
    }
    return Result.error("原密码有误，修改失败");
  }
}
