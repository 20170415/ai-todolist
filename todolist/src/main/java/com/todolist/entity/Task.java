package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_tasks")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userid;

    private String title;

    private String description;

    private Long groupid;

    private Integer priority;

    private Integer status;

    private LocalDateTime duedate;

    private LocalDateTime completedtime;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    private Long createuser;

    private Long updateuser;

}