package com.ysl.easyconnection.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysl.easyconnection.entity.User;
import com.ysl.easyconnection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/19 15:02
 * @modifyTime :
 * @description :
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/get")
    public User get(@RequestBody User param){
        User byId = userService.findById(param);
        return byId;
    }

}
