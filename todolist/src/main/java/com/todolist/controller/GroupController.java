package com.todolist.controller;

import com.todolist.common.Result;
import com.todolist.dto.GroupDTO;
import com.todolist.service.GroupService;
import com.todolist.vo.GroupVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public Result<List<GroupVO>> getGroupList(@RequestAttribute Long userId) {
        List<GroupVO> list = groupService.getGroupList(userId);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<GroupVO> getGroup(@PathVariable Long id, @RequestAttribute Long userId) {
        GroupVO group = groupService.getGroupById(id, userId);
        return Result.success(group);
    }

    @PostMapping
    public Result<Void> createGroup(@Valid @RequestBody GroupDTO dto, @RequestAttribute Long userId) {
        groupService.createGroup(dto, userId);
        return Result.success("创建成功");
    }

    @PutMapping("/{id}")
    public Result<Void> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupDTO dto,
                                    @RequestAttribute Long userId) {
        groupService.updateGroup(id, dto, userId);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteGroup(@PathVariable Long id, @RequestAttribute Long userId) {
        groupService.deleteGroup(id, userId);
        return Result.success("删除成功");
    }

}