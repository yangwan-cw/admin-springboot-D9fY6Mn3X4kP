package com.ioomex.ioomexadminspringboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ioomex.ioomexadminspringboot.constant.UserModeConstant;
import com.ioomex.ioomexadminspringboot.mapper.UserMapper;
import com.ioomex.ioomexadminspringboot.module.User;
import com.ioomex.ioomexadminspringboot.module.request.UserDeleteRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserLoginRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserRegisterRequest;
import com.ioomex.ioomexadminspringboot.module.request.UserSearchRequest;
import com.ioomex.ioomexadminspringboot.service.UserService;
import com.ioomex.ioomexadminspringboot.service.impl.UserServiceImpl;
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
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (ObjectUtils.isEmpty(userRegisterRequest)) {
            return null;
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyEmpty(username, password, checkPassword)) {
            return null;
        }
        return userService.userRegister(userRegisterRequest.getUsername(), userRegisterRequest.getPassword(),
          userRegisterRequest.getCheckPassword());
    }


    @GetMapping("/current")
    public User current(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserModeConstant.USER_LOGIN_STATUS);
        User user = (User) userObject;
        if (user == null) {
            return null;
        }
        long userId = user.getId();
        User user1 = this.userMapper.selectById(userId);
        return UserServiceImpl.getSafeUser(user1);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(userLoginRequest)) {
            return null;
        }
//        if(true){
//            return new User();
//        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyEmpty(username, password)) {
            return null;
        }
        return userService.doLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword(), request);
    }

    @PostMapping("/search")
    public List<User> searchUser(@RequestBody UserSearchRequest userSearchRequest,
                                 HttpServletRequest request) {

        if (isAdmin(request)) {
            return null;
        }


        if (ObjectUtils.isEmpty(userSearchRequest)) {
            return null;
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
        return users;
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
    public boolean deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (isAdmin(request)) {
            return false;
        }


        if (ObjectUtils.isEmpty(userDeleteRequest)) {
            return false;
        }
        Integer id = userDeleteRequest.getId();

        // 先查询是否存在该用户
        User user = this.userMapper.selectById(id);
        if (user == null) {
            return false;
        }

        if (id != null) {
            this.userMapper.deleteById(id);
        }
        return false;
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public int userLogout(HttpServletRequest request) {
        int result = userService.userLogout(request);
        return result;
    }

}