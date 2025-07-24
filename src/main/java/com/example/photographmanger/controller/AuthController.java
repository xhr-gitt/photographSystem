package com.example.photographmanger.controller;

import com.example.photographmanger.Util.JwtUtil;
import com.example.photographmanger.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; // 添加这行

    // 修改构造函数
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil; // 添加这行
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> credentials) {  // 直接使用Map接收
        System.out.println("收到登录请求，用户名：" + credentials.get("username"));
        Map<String, Object> response = new HashMap<>();
        try {
            // 从Map中获取用户名和密码
            String username = credentials.get("username");
            String password = credentials.get("password");

            // 验证参数非空
            if (username == null || password == null) {
                response.put("error", "用户名和密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // 获取用户详细信息
            User userDetails = (User) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("USER"); // 默认角色

            String token = jwtUtil.generateToken(authentication);
            response.put("message", "登录成功");
            response.put("token", token);
            response.put("role",role);
            response.put("userId", userDetails.getId()); // 直接从User对象获取ID

            System.out.println("登录成功，用户ID: " + userDetails.getId());
            System.out.println("token 是"+token);
            System.out.println("role是"+role);
            System.out.println("登录响应数据: " + response); // 确认包含userId

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(response);

        } catch (AuthenticationException e) {
            response.put("error", "用户名或密码错误");
            return ResponseEntity.status(401).body(response);
        }
    }

}