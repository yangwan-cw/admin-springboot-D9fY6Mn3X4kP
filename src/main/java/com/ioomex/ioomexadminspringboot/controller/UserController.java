package com.ioomex.ioomexadminspringboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ioomex.ioomexadminspringboot.common.ErrorCode;
import com.ioomex.ioomexadminspringboot.constant.UserModeConstant;
import com.ioomex.ioomexadminspringboot.mapper.UserMapper;
import com.ioomex.ioomexadminspringboot.module.User;
import com.ioomex.ioomexadminspringboot.module.request.UserDeleteRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserLoginRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserRegisterRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserSearchRequest;
import com.ioomex.ioomexadminspringboot.service.UserService;
import com.ioomex.ioomexadminspringboot.service.impl.UserServiceImpl;
import com.ioomex.ioomexadminspringboot.util.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public Object register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (ObjectUtils.isEmpty(userRegisterRequest)) {
            return ResultUtils.fail(ErrorCode.NULL_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyEmpty(username, password, checkPassword)) {
            return ResultUtils.fail(ErrorCode.PARAM_ERROR);
        }
        long result = userService.userRegister(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(),
          userRegisterRequest.getCheckPassword());
        return ResultUtils.success(result);
    }


    @GetMapping("/current")
    public Object current(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserModeConstant.USER_LOGIN_STATUS);
        User user = (User) userObject;
        if (user == null) {
            return ResultUtils.fail(ErrorCode.NULL_ERROR);
        }
        long userId = user.getId();
        User user1 = this.userMapper.selectById(userId);
        User safeUser = UserServiceImpl.getSafeUser(user1);
        return ResultUtils.success(safeUser);
    }

    @PostMapping("/login")
    public Object userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(userLoginRequest)) {
            return ResultUtils.fail(ErrorCode.NULL_ERROR);
        }
//        if(true){
//            return new User();
//        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyEmpty(username, password)) {
            return ResultUtils.fail(ErrorCode.PARAM_ERROR);
        }
        User user = userService.doLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword(), request);
        return ResultUtils.success(user);
    }

    @PostMapping("/search")
    public Object searchUser(@RequestBody UserSearchRequest userSearchRequest,
                             HttpServletRequest request) {

        if (isAdmin(request)) {
            return ResultUtils.fail(ErrorCode.NO_AUTH_ERROR);
        }


        if (ObjectUtils.isEmpty(userSearchRequest)) {
            return ResultUtils.fail(ErrorCode.PARAM_ERROR);
        }
        String username = userSearchRequest.getUserName();
        List<User> users = this.userMapper.selectList(new QueryWrapper<User>().lambda().like(ObjectUtils.isNotEmpty(username),
          User::getUsername,
          username));

        // 设置用户密码为空
        if (CollUtil.isNotEmpty(users)) {
            users.stream().map(user -> {
                UserServiceImpl.getSafeUser(user);
                return user;
            });
        }
        return ResultUtils.success(users);
    }

    private static boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObject = request.getSession().getAttribute(UserModeConstant.USER_LOGIN_STATUS);
        User user = (User) userObject;
        if (ObjectUtils.isEmpty(user) || !Objects.equals(user.getUserRole(), UserModeConstant.ROLE_ADMIN)) {
            return true;
        }
        return false;
    }

    @PostMapping("/delete")
    public Object deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (isAdmin(request)) {
            return ResultUtils.fail(ErrorCode.NO_AUTH_ERROR);
        }


        if (ObjectUtils.isEmpty(userDeleteRequest)) {
            return ResultUtils.fail(ErrorCode.PARAM_ERROR);
        }
        Integer id = userDeleteRequest.getId();

        // 先查询是否存在该用户
        User user = this.userMapper.selectById(id);
        if (user == null) {
            return ResultUtils.fail(ErrorCode.NULL_ERROR);
        }

        if (id != null) {
            int i = this.userMapper.deleteById(id);
            return ResultUtils.success(i);
        }
        return ResultUtils.success(false);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Object userLogout(HttpServletRequest request) {
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

}