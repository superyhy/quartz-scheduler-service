package com.keeson.quartzschedulerservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Yhy
 * @create 2025/10/29 11:14
 * @describe
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginMap, HttpSession session) {
        String username = loginMap.get("username");
        String password = loginMap.get("password");

        Map<String, Object> result = new HashMap<>();
        if ("keeson".equals(username) && "keeson".equals(password)) {
            // 登录成功，保存 session
            session.setAttribute("user", username);
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        return result;
    }
}
