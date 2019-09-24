package com.hl.springboot_shiro_jwt.controller;

import com.hl.springboot_shiro_jwt.domain.User;
import com.hl.springboot_shiro_jwt.repository.UserRepository;
import com.hl.springboot_shiro_jwt.shiro.JwtConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    public String login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return "参数错误";
        }

        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            return "用户名或密码错误";
        }

        String token = jwtConfig.createToken(user);

        return token;
    }

    @PostMapping("/unLogin")
    public String unLogin() {
        return "未登录";
    }

    @PostMapping("/info")
    public String info() {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        return username;
    }
}
