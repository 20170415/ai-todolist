package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todolist.dto.UserDTO;
import com.todolist.entity.User;
import com.todolist.exception.BusinessException;
import com.todolist.mapper.UserMapper;
import com.todolist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserVO> getUserPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreatetime);

        Page<User> userPage = userMapper.selectPage(page, wrapper);

        Page<UserVO> voPage = new Page<>(pageNum, pageSize);
        voPage.setTotal(userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));

        return voPage;
    }

    public List<UserVO> getUserList() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreatetime);
        return userMapper.selectList(wrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Transactional
    public void createUser(UserDTO dto, Long createUserId) {
        User existUser = userMapper.selectById(dto.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setRole(dto.getRole());
        user.setEnabled(dto.getEnabled() != null && dto.getEnabled() ? 1 : 0);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setDeleted(0);
        user.setCreatetime(LocalDateTime.now());
        user.setUpdatetime(LocalDateTime.now());
        user.setCreateuser(createUserId);
        user.setUpdateuser(createUserId);

        userMapper.insert(user);
    }

    @Transactional
    public void updateUser(Long id, UserDTO dto, Long updateUserId) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!user.getUsername().equals(dto.getUsername())) {
            User existUser = userMapper.selectById(dto.getUsername());
            if (existUser != null) {
                throw new BusinessException("用户名已存在");
            }
        }

        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setRole(dto.getRole());
        if (dto.getEnabled() != null) {
            user.setEnabled(dto.getEnabled() ? 1 : 0);
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setUpdatetime(LocalDateTime.now());
        user.setUpdateuser(updateUserId);

        userMapper.updateById(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    @Transactional
    public void resetPassword(Long id, Long updateUserId) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode("123456"));
        user.setUpdatetime(LocalDateTime.now());
        user.setUpdateuser(updateUserId);

        userMapper.updateById(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatetime(LocalDateTime.now());
        user.setUpdateuser(userId);

        userMapper.updateById(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo, "enabled");
        vo.setEnabled(user.getEnabled() != null && user.getEnabled() == 1);
        return vo;
    }

}