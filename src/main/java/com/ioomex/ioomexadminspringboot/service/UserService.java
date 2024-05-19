package com.ioomex.ioomexadminspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ioomex.ioomexadminspringboot.module.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-05-18 16:32:38
 */
public interface UserService extends IService<User> {


    /**
     * 前端请求用户注册接口,传递用户名、密码、验证码
     * 需要校验用户名
     *
     * @param username  用户名
     * @param password  密码
     * @param checkCode 验证码
     * @return 用户id
     */
    long userRegister(String username, String password, String checkCode);


    /**
     * 前端请求用户登录接口,传递用户名、密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User doLogin(String username, String password, HttpServletRequest request);
}
