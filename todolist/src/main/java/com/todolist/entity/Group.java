package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_groups")
public class Group {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userid;

    private String name;

    private String color;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    private Long createuser;

    private Long updateuser;

}