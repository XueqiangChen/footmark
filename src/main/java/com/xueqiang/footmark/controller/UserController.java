package com.xueqiang.footmark.controller;

import com.xueqiang.footmark.model.User;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/user")
public class UserController {

//    @ApiOperation("新增用户接口")
    @PostMapping("/add")
    public boolean addUser(@RequestBody User user) {
        return false;
    }

//    @ApiOperation("通过id查找用户接口")
    @GetMapping("/find/{id}")
    public User findById(@PathVariable("id") int id) {
        return new User();
    }

//    @ApiOperation("更新用户信息接口")
    @PutMapping("/update")
    public boolean update(@RequestBody User user) {
        return true;
    }

//    @ApiOperation("删除用户接口")
    @DeleteMapping("/delete/{id}")
//    @ApiIgnore
    public boolean delete(@PathVariable("id") int id) {
        return true;
    }
}
