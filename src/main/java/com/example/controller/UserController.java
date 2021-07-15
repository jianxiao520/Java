package com.example.controller;


import com.example.annotation.PassToken;
import com.example.annotation.UserLoginToken;
import com.example.pojo.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Resource
    private UserService userService;

    // 注册账户
    @PostMapping("/create")
//    @Validated
    public ResponseEntity<Map<String, Object>> createAccount(@Valid User user) {
        return userService.createAccount(user);
    }

    // 登录接口
    @PassToken
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAccount(@Valid User user) {
        return userService.loginAccount(user);
    }

    // 激活接口
    @GetMapping("/activation")
    public ResponseEntity<Map<String, Object>> activationAccount(String confirmCode) {
        return userService.activationAccount(confirmCode);
    }

    // 校验 Token 接口
    @UserLoginToken
    @RequestMapping("/verifyUser")
    public ResponseEntity<Map<String, Object>> CheckLogin() {
        return userService.verifyUserIsLogin();
    }

    // 测试接口
    @RequestMapping("/list")
    public List<User> queryUser() {
        return userService.getUsers();
    }
}
