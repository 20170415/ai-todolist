package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.GroupDTO;
import com.todolist.entity.Group;
import com.todolist.exception.BusinessException;
import com.todolist.mapper.GroupMapper;
import com.todolist.vo.GroupVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupMapper groupMapper;

    public List<GroupVO> getGroupList(Long userId) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.isNull(Group::getUserid).or().eq(Group::getUserid, userId));
        wrapper.orderByAsc(Group::getUserid);
        wrapper.orderByDesc(Group::getCreatetime);

        return groupMapper.selectList(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public GroupVO getGroupById(Long id, Long userId) {
        Group group = groupMapper.selectById(id);
        if (group == null) {
            throw new BusinessException("分组不存在");
        }

        if (group.getUserid() != null && !group.getUserid().equals(userId)) {
            throw new BusinessException("无权访问该分组");
        }

        return convertToVO(group);
    }

    @Transactional
    public void createGroup(GroupDTO dto, Long userId) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Group::getUserid, userId);
        wrapper.eq(Group::getName, dto.getName());
        if (groupMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("分组名称已存在");
        }

        Group group = new Group();
        BeanUtils.copyProperties(dto, group);
        group.setUserid(userId);
        group.setDeleted(0);
        group.setCreatetime(LocalDateTime.now());
        group.setUpdatetime(LocalDateTime.now());
        group.setCreateuser(userId);
        group.setUpdateuser(userId);

        groupMapper.insert(group);
    }

    @Transactional
    public void updateGroup(Long id, GroupDTO dto, Long userId) {
        Group group = groupMapper.selectById(id);
        if (group == null) {
            throw new BusinessException("分组不存在");
        }

        if (group.getUserid() == null) {
            throw new BusinessException("系统默认分组不可修改");
        }

        if (!group.getUserid().equals(userId)) {
            throw new BusinessException("无权修改该分组");
        }

        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Group::getUserid, userId);
        wrapper.eq(Group::getName, dto.getName());
        wrapper.ne(Group::getId, id);
        if (groupMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("分组名称已存在");
        }

        BeanUtils.copyProperties(dto, group, "id", "userid", "deleted", "createtime", "createuser");
        group.setUpdatetime(LocalDateTime.now());
        group.setUpdateuser(userId);

        groupMapper.updateById(group);
    }

    @Transactional
    public void deleteGroup(Long id, Long userId) {
        Group group = groupMapper.selectById(id);
        if (group == null) {
            throw new BusinessException("分组不存在");
        }

        if (group.getUserid() == null) {
            throw new BusinessException("系统默认分组不可删除");
        }

        if (!group.getUserid().equals(userId)) {
            throw new BusinessException("无权删除该分组");
        }

        groupMapper.deleteById(id);
    }

    private GroupVO convertToVO(Group group) {
        GroupVO vo = new GroupVO();
        BeanUtils.copyProperties(group, vo);
        return vo;
    }

}