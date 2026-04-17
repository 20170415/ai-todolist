package com.todolist.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.common.Result;
import com.todolist.dto.UserDTO;
import com.todolist.service.AdminService;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import com.todolist.vo.TaskVO;
import com.todolist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;
    private final AdminService adminService;

    @GetMapping("/users")
    public Result<List<UserVO>> getUserList() {
        List<UserVO> list = userService.getUserList();
        return Result.success(list);
    }

    @GetMapping("/users/page")
    public Result<Page<UserVO>> getUserPage(@RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        Page<UserVO> page = userService.getUserPage(pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/users")
    public Result<Void> createUser(@Valid @RequestBody UserDTO dto, @RequestAttribute Long userId) {
        userService.createUser(dto, userId);
        return Result.success("创建成功");
    }

    @PutMapping("/users/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO dto,
                                   @RequestAttribute Long userId) {
        userService.updateUser(id, dto, userId);
        return Result.success("更新成功");
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }

    @PostMapping("/users/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestAttribute Long userId) {
        userService.resetPassword(id, userId);
        return Result.success("密码已重置为123456");
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = adminService.getStats();
        return Result.success(stats);
    }

    @GetMapping("/stats/tasks")
    public Result<Map<String, Object>> getTaskStats() {
        Map<String, Object> stats = adminService.getTaskStats();
        return Result.success(stats);
    }

    @GetMapping("/tasks")
    public Result<List<TaskVO>> getAllTasks() {
        List<TaskVO> list = taskService.getAllTaskList();
        return Result.success(list);
    }

    @GetMapping("/tasks/shelved")
    public Result<List<TaskVO>> getAllShelvedTasks() {
        List<TaskVO> list = taskService.getAllShelvedTaskList();
        return Result.success(list);
    }

}