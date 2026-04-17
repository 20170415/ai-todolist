package com.todolist.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GroupDTO {

    @NotBlank(message = "分组名称不能为空")
    private String name;

    private String color;

}