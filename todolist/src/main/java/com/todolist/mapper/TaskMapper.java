package com.todolist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.entity.Task;
import com.todolist.vo.TaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    List<TaskVO> selectTaskVOList(@Param("userid") Long userid, @Param("status") Integer status);

    List<TaskVO> selectAllTaskVOList(@Param("status") Integer status);

    IPage<TaskVO> selectTaskVOPage(Page<TaskVO> page, @Param("userid") Long userid,
                                   @Param("status") Integer status, @Param("groupid") Long groupid,
                                   @Param("priority") Integer priority);

}