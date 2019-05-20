package com.ysl.easyconnection.service;

import com.ysl.easyconnection.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/19 15:05
 * @modifyTime :
 * @description :
 */
@Service
public class UserService {
    public User findById(User user) {
        user.setId("newId: " + user.getId());
        user.setName("newName: " + user.getName());
        return user;
    }
}
