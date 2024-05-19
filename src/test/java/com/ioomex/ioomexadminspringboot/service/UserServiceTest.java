package com.ioomex.ioomexadminspringboot.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.ioomex.ioomexadminspringboot.module.User;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUsers() {
        User user = new User();
        user.setUsername("");
        user.setUseraccount("");
        user.setAvatarurl("");
        user.setGender(0);
        user.setUserpassword("");
        user.setPhone("");
        user.setEmail("");
        user.setUserstatus(0);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        user.setIsdelete(0);
        userService.save(user);
    }

    @Test
    public void messageDigestPassword() throws NoSuchAlgorithmException {
        String s = DigestUtils.md5DigestAsHex("9978@wzb".getBytes(StandardCharsets.UTF_8));
        log.info(s);

    }
}