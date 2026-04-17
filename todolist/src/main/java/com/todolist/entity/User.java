package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String role;

    private Integer enabled;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    private Long createuser;

    private Long updateuser;

}