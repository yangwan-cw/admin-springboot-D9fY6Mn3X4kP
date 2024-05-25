package com.ioomex.ioomexadminspringboot.service.impl;

import java.util.Date;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ioomex.ioomexadminspringboot.constant.UserEncryptConstant;
import com.ioomex.ioomexadminspringboot.constant.UserModeConstant;
import com.ioomex.ioomexadminspringboot.mapper.UserMapper;
import com.ioomex.ioomexadminspringboot.module.User;
import com.ioomex.ioomexadminspringboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-05-18 16:32:38
 */
@Service
@Slf4j
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
        Integer x = registerValidate(username, password, checkPassword);
        if (x != null && x!=0) return x;

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
     * 前端请求用户登录接口,传递用户名、密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回用户脱敏的信息
     */
    @Override
    public User doLogin(String username, String password, HttpServletRequest request) {
        // 校验参数
        if (StringUtils.isAnyEmpty(username, password)) {
            return null;
        }

        // 校验用户名
        if (username.length() < 4) {
            return null;
        }

        // 校验密码
        if (password.length() < 8) {
            return null;
        }
        // 校验密码是否包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return null;
        }

        String passwordEncrypt =
          DigestUtils.md5DigestAsHex((UserEncryptConstant.SALT + password).getBytes(StandardCharsets.UTF_8));
        // 根据加密的密码去数据库查询
        User user = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUseraccount, username).eq(User::getUserpassword,
          passwordEncrypt));
        if (ObjectUtils.isEmpty(user)) {
            log.info("user login failed,userAccount cannot match userPassWord {}", user);
            return null;
        }

        User safeUser = getSafeUser(user);

        // 记住用户的登录态
        request.getSession().setAttribute(UserModeConstant.USER_LOGIN_STATUS, safeUser);

        return safeUser;
    }


    public static User getSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUseraccount(user.getUseraccount());
        safeUser.setAvatarurl(user.getAvatarurl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setEmail(user.getEmail());
        safeUser.setCreatetime(user.getCreatetime());
        safeUser.setUpdatetime(user.getUpdatetime());
        return safeUser;
    }


    /**
     * 校验参数
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 密码检查
     * @return 返回值
     */
    private Integer registerValidate(String username, String password, String checkPassword) {
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
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()) {
            return -1;
        }

        // 判断密码和验证码是否相同
        if (!ObjUtil.equals(password, checkPassword)) {
            return -1;
        }

        // 校验用户名是否重复
        User one = this.getOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if (!ObjectUtils.isEmpty(one)) {
            return -1;
        }
        return 0;
    }


    /**
     * 密码加密
     *
     * @param password 需要加密的密码
     */
    private static String passwordEncrypt(String password) {
        return DigestUtils.md5DigestAsHex((UserEncryptConstant.SALT + password).getBytes(StandardCharsets.UTF_8));
    }



    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(UserModeConstant.USER_LOGIN_STATUS);
        return 1;
    }


}




