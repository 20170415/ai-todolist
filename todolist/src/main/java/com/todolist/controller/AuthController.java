package com.todolist.controller;

import com.todolist.common.Result;
import com.todolist.dto.LoginDTO;
import com.todolist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute Long userId) {
        authService.logout(userId);
        return Result.success("登出成功");
    }

    @PostMapping("/refresh")
    public Result<Map<String, Object>> refreshToken(@RequestAttribute Long userId) {
        Map<String, Object> data = authService.refreshToken(userId);
        return Result.success(data);
    }

}