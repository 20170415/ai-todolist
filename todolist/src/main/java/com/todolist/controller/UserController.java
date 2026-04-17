package com.todolist.controller;

import com.todolist.common.Result;
import com.todolist.service.UserService;
import com.todolist.vo.UserVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Data
class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public Result<UserVO> getUserInfo(@RequestAttribute Long userId) {
        UserVO user = userService.getUserById(userId);
        return Result.success(user);
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestAttribute Long userId,
                                       @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return Result.success("密码修改成功");
    }

}