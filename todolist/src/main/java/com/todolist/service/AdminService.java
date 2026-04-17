package com.todolist.service;

import com.todolist.common.enums.StatusEnum;
import com.todolist.mapper.TaskMapper;
import com.todolist.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskService taskService;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        long userCount = userMapper.selectCount(null);
        stats.put("userCount", userCount);

        long totalTaskCount = taskMapper.selectCount(null);
        stats.put("totalTaskCount", totalTaskCount);

        long completedCount = taskMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.todolist.entity.Task>()
                        .eq(com.todolist.entity.Task::getStatus, StatusEnum.COMPLETED.getCode()));

        stats.put("completedCount", completedCount);

        double completionRate = totalTaskCount > 0 ? (completedCount * 100.0 / totalTaskCount) : 0;
        stats.put("completionRate", Math.round(completionRate * 100) / 100.0);

        return stats;
    }

    public Map<String, Object> getTaskStats() {
        Map<String, Object> stats = new HashMap<>();

        for (StatusEnum status : StatusEnum.values()) {
            long count = taskMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.todolist.entity.Task>()
                            .eq(com.todolist.entity.Task::getStatus, status.getCode()));
            stats.put(status.getDesc() + "Count", count);
        }

        return stats;
    }

}