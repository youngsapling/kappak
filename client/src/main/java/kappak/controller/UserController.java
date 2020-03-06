package kappak.controller;

import kappak.entity.User;
import kappak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public User find(@RequestParam String id, @RequestParam String name, @RequestParam Integer age){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        return user;
    }
    @PostMapping("/{number}")
    public String number(@PathVariable(value = "number") String number){
        return String.format("restful : [%s]", number);
    }
}
