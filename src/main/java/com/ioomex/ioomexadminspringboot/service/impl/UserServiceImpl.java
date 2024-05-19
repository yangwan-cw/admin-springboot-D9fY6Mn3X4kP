package com.ioomex.ioomexadminspringboot.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ioomex.ioomexadminspringboot.mapper.UserMapper;
import com.ioomex.ioomexadminspringboot.module.User;
import com.ioomex.ioomexadminspringboot.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-05-18 16:32:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 前端请求用户注册接口,传递用户名、密码、验证码
     * 需要校验用户名
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 验证码
     * @return 用户id
     */
    @Override
    public long userRegister(String username, String password, String checkPassword) {
        Integer x = getInteger(username, password, checkPassword);
        if (x != null) return x;

        String encryptPassword = passwordEncrypt(password);
        User user = new User();
        user.setUseraccount(username);
        user.setUserpassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            return -1;
        }
        return user.getId();
    }


    /**
     * 校验参数
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 密码检查
     * @return 返回值
     */
    private Integer getInteger(String username, String password, String checkPassword) {
        // 校验参数
        if (StringUtils.isAnyEmpty(username, password, checkPassword)) {
            return -1;
        }

        // 校验用户名
        if (username.length() < 4) {
            return -1;
        }

        // 校验密码
        if (password.length() < 8 || checkPassword.length() > 16) {
            return -1;
        }
        // 校验密码是否包含特殊字符
        String reg = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(reg).matcher(username);
        if (!matcher.find()) {
            return -1;
        }

        // 判断密码和验证码是否相同
        if (ObjUtil.equals(password, checkPassword)) {
            return -1;
        }

        // 校验用户名是否重复
        User one = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (ObjectUtils.isEmpty(one)) {
            return -1;
        }
        return null;
    }


    /**
     * 密码加密
     *
     * @param password 需要加密的密码
     */
    private static String passwordEncrypt(String password) {
        final String SALT = "IOOMEX";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
    }

}




