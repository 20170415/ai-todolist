package com.todolist.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupVO {

    private Long id;

    private Long userid;

    private String name;

    private String color;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

}