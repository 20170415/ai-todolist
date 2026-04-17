package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.enums.StatusEnum;
import com.todolist.dto.BatchStatusDTO;
import com.todolist.dto.TaskDTO;
import com.todolist.entity.Task;
import com.todolist.exception.BusinessException;
import com.todolist.mapper.TaskMapper;
import com.todolist.vo.TaskVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;

    public List<TaskVO> getTaskList(Long userId) {
        return taskMapper.selectTaskVOList(userId, null);
    }

    public List<TaskVO> getShelvedTaskList(Long userId) {
        return taskMapper.selectTaskVOList(userId, StatusEnum.SHELVED.getCode());
    }

    public List<TaskVO> getAllTaskList() {
        return taskMapper.selectAllTaskVOList(null);
    }

    public List<TaskVO> getAllShelvedTaskList() {
        return taskMapper.selectAllTaskVOList(StatusEnum.SHELVED.getCode());
    }

    public TaskVO getTaskById(Long id, Long userId) {
        Task task = taskMapper.selectById(id);
        if (task == null || !task.getUserid().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        List<TaskVO> list = taskMapper.selectTaskVOList(userId, null);
        return list.stream()
                .filter(vo -> vo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("任务不存在"));
    }

    @Transactional
    public void createTask(TaskDTO dto, Long userId) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setUserid(userId);
        task.setDeleted(0);
        task.setCreatetime(LocalDateTime.now());
        task.setUpdatetime(LocalDateTime.now());
        task.setCreateuser(userId);
        task.setUpdateuser(userId);

        if (task.getStatus() == null) {
            task.setStatus(StatusEnum.TODO.getCode());
        }

        taskMapper.insert(task);
    }

    @Transactional
    public void updateTask(Long id, TaskDTO dto, Long userId) {
        Task task = taskMapper.selectById(id);
        if (task == null || !task.getUserid().equals(userId)) {
            throw new BusinessException("任务不存在");
        }

        BeanUtils.copyProperties(dto, task, "id", "userid", "deleted", "createtime", "createuser");

        if (dto.getStatus() != null && dto.getStatus() == StatusEnum.COMPLETED.getCode()
                && task.getStatus() != StatusEnum.COMPLETED.getCode()) {
            task.setCompletedtime(LocalDateTime.now());
        }

        task.setUpdatetime(LocalDateTime.now());
        task.setUpdateuser(userId);

        taskMapper.updateById(task);
    }

    @Transactional
    public void deleteTask(Long id, Long userId) {
        Task task = taskMapper.selectById(id);
        if (task == null || !task.getUserid().equals(userId)) {
            throw new BusinessException("任务不存在");
        }
        taskMapper.deleteById(id);
    }

    @Transactional
    public void batchUpdateStatus(BatchStatusDTO dto, Long userId) {
        for (Long taskId : dto.getTaskIds()) {
            Task task = taskMapper.selectById(taskId);
            if (task == null || !task.getUserid().equals(userId)) {
                continue;
            }

            task.setStatus(dto.getStatus());

            if (dto.getStatus() == StatusEnum.COMPLETED.getCode()) {
                task.setCompletedtime(LocalDateTime.now());
            } else if (dto.getStatus() != StatusEnum.COMPLETED.getCode()) {
                task.setCompletedtime(null);
            }

            task.setUpdatetime(LocalDateTime.now());
            task.setUpdateuser(userId);

            taskMapper.updateById(task);
        }
    }

    public long countByStatus(Long userId, Integer status) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserid, userId);
        if (status != null) {
            wrapper.eq(Task::getStatus, status);
        }
        return taskMapper.selectCount(wrapper);
    }

}