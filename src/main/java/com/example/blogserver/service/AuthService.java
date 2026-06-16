package com.example.blogserver.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.common.ResultCode;
import com.example.blogserver.common.exception.BusinessException;
import com.example.blogserver.config.properties.JwtProperties;
import com.example.blogserver.dto.auth.LoginDTO;
import com.example.blogserver.dto.auth.RegisterDTO;
import com.example.blogserver.dto.auth.ResetPasswordDTO;
import com.example.blogserver.dto.auth.SendCodeDTO;
import com.example.blogserver.entity.User;
import com.example.blogserver.entity.UserRole;
import com.example.blogserver.mapper.UserMapper;
import com.example.blogserver.mapper.UserRoleMapper;
import com.example.blogserver.security.JwtUtil;
import com.example.blogserver.security.LoginUser;
import com.example.blogserver.vo.LoginVO;
import com.example.blogserver.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * 认证服务：注册、登录、验证码、找回/重置密码
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    /** 普通用户角色ID（见 data.sql） */
    private static final Long ROLE_USER_ID = 2L;
    private static final String TYPE_REGISTER = "register";
    private static final String TYPE_RESET = "reset";

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final MailService mailService;
    private final VerifyCodeStore verifyCodeStore;

    /**
     * 发送邮箱验证码
     */
    public void sendCode(SendCodeDTO dto) {
        User existing = getByEmail(dto.getEmail());
        if (TYPE_REGISTER.equals(dto.getType()) && existing != null) {
            throw new BusinessException(ResultCode.EMAIL_ALREADY_EXIST);
        }
        if (TYPE_RESET.equals(dto.getType()) && existing == null) {
            throw new BusinessException(ResultCode.EMAIL_NOT_EXIST);
        }
        String code = RandomUtil.randomNumbers(6);
        verifyCodeStore.save(dto.getType(), dto.getEmail(), code);
        String scene = TYPE_REGISTER.equals(dto.getType()) ? "注册账号" : "重置密码";
        mailService.sendVerifyCode(dto.getEmail(), code, "【博客系统】" + scene + "验证码", scene);
        log.info("已向 {} 发送{}验证码", dto.getEmail(), scene);
    }

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        if (!verifyCodeStore.verify(TYPE_REGISTER, dto.getEmail(), dto.getCode())) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }
        if (getByEmail(dto.getEmail()) != null) {
            throw new BusinessException(ResultCode.EMAIL_ALREADY_EXIST);
        }
        if (userMapper.exists(Wrappers.<User>lambdaQuery().eq(User::getUsername, dto.getUsername()))) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXIST);
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getUsername());
        user.setStatus(1);
        user.setGender(0);
        userMapper.insert(user);
        // 分配普通用户角色
        userRoleMapper.insert(new UserRole(user.getId(), ROLE_USER_ID));
        log.info("新用户注册成功：{}（id={}）", user.getEmail(), user.getId());
    }

    /**
     * 登录
     */
    public LoginVO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }
        // 更新登录信息
        User update = new User();
        update.setId(user.getId());
        update.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(update);

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        log.info("用户登录成功：{}（id={}）", user.getEmail(), user.getId());
        return LoginVO.builder()
                .token(token)
                .tokenPrefix(jwtProperties.getTokenPrefix())
                .expiresIn(jwtProperties.getExpire() / 1000)
                .userInfo(toUserInfoVO(loginUser))
                .build();
    }

    /**
     * 重置密码（忘记密码）
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordDTO dto) {
        if (!verifyCodeStore.verify(TYPE_RESET, dto.getEmail(), dto.getCode())) {
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }
        User user = getByEmail(dto.getEmail());
        if (user == null) {
            throw new BusinessException(ResultCode.EMAIL_NOT_EXIST);
        }
        User update = new User();
        update.setId(user.getId());
        update.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(update);
        log.info("用户重置密码成功：{}", user.getEmail());
    }

    /**
     * 由登录主体构建用户信息 VO
     */
    public UserInfoVO toUserInfoVO(LoginUser loginUser) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(loginUser.getUser(), vo);
        vo.setRoles(new HashSet<>(loginUser.getRoles()));
        vo.setPermissions(new HashSet<>(loginUser.getPermissions()));
        return vo;
    }

    private User getByEmail(String email) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
    }
}
