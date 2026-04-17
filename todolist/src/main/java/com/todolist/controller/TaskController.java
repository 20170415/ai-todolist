package com.todolist.controller;

import com.todolist.common.Result;
import com.todolist.dto.BatchStatusDTO;
import com.todolist.dto.TaskDTO;
import com.todolist.service.TaskService;
import com.todolist.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Result<List<TaskVO>> getTaskList(@RequestAttribute Long userId) {
        List<TaskVO> list = taskService.getTaskList(userId);
        return Result.success(list);
    }

    @GetMapping("/shelved")
    public Result<List<TaskVO>> getShelvedTaskList(@RequestAttribute Long userId) {
        List<TaskVO> list = taskService.getShelvedTaskList(userId);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<TaskVO> getTask(@PathVariable Long id, @RequestAttribute Long userId) {
        TaskVO task = taskService.getTaskById(id, userId);
        return Result.success(task);
    }

    @PostMapping
    public Result<Void> createTask(@Valid @RequestBody TaskDTO dto, @RequestAttribute Long userId) {
        taskService.createTask(dto, userId);
        return Result.success("创建成功");
    }

    @PutMapping("/{id}")
    public Result<Void> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO dto,
                                   @RequestAttribute Long userId) {
        taskService.updateTask(id, dto, userId);
        return Result.success("更新成功");
    }

    @PutMapping("/batch-status")
    public Result<Void> batchUpdateStatus(@Valid @RequestBody BatchStatusDTO dto,
                                          @RequestAttribute Long userId) {
        taskService.batchUpdateStatus(dto, userId);
        return Result.success("批量更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTask(@PathVariable Long id, @RequestAttribute Long userId) {
        taskService.deleteTask(id, userId);
        return Result.success("删除成功");
    }

    @GetMapping("/stats")
    public Result<Map<String, Long>> getStats(@RequestAttribute Long userId) {
        Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("todoCount", taskService.countByStatus(userId, 1));
        stats.put("inProgressCount", taskService.countByStatus(userId, 2));
        stats.put("completedCount", taskService.countByStatus(userId, 3));
        stats.put("shelvedCount", taskService.countByStatus(userId, 4));
        stats.put("totalCount", taskService.countByStatus(userId, null));
        return Result.success(stats);
    }

}