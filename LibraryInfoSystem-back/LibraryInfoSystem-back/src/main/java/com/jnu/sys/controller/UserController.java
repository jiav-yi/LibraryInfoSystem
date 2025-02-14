package com.jnu.sys.controller;

import com.jnu.commmon.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public Result<Map<String, Object>> login() {
        Map<String, Object> data = new HashMap<>();
        data.put("token", "admin-token");
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getInfo() {
        Map<String, Object> data = new HashMap<>();
        data.put("roles", "['admin']");
        data.put("introduction", "I am a super administrator");
        data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        data.put("name", "Super Admin");
        return Result.success(data);
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        return Result.success("注销成功");
    }
}
