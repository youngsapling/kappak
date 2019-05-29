package kappak.controller;

import kappak.entity.User;
import kappak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/find")
    public List<User> find(User one){
        one.setId("new" + one.getId());
//        two.setId("new" + two.getId());
        List resultList = new ArrayList(2);
        resultList.add(one);
//        resultList.add(two);
        return resultList;
    }
}
