package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.common.Constants;
import com.todolist.common.Result;
import com.todolist.dto.LoginDTO;
import com.todolist.dto.UserDTO;
import com.todolist.entity.User;
import com.todolist.exception.BusinessException;
import com.todolist.mapper.UserMapper;
import com.todolist.security.JwtUtils;
import com.todolist.utils.RedisUtils;
import com.todolist.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    public Result<Map<String, Object>> login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null || user.getEnabled() == 0) {
            throw new BusinessException("用户不存在或已禁用");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

        String redisKey = Constants.REDIS_TOKEN_PREFIX + user.getId();
        redisUtils.set(redisKey, token, Constants.TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", convertToVO(user));

        return Result.success("登录成功", data);
    }

    public void logout(Long userId) {
        String redisKey = Constants.REDIS_TOKEN_PREFIX + userId;
        redisUtils.delete(redisKey);
    }

    public Map<String, Object> refreshToken(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

        String redisKey = Constants.REDIS_TOKEN_PREFIX + user.getId();
        redisUtils.set(redisKey, token, Constants.TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return data;
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

}