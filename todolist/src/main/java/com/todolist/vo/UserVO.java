package com.todolist.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String nickname;

    private String role;

    private Boolean enabled;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

}