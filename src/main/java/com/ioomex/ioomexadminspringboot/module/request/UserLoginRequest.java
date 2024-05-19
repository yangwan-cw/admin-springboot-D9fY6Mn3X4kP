package com.ioomex.ioomexadminspringboot.module.request;

import lombok.Data;

import java.io.Serializable;

/**
 * UserLoginRequest
 *
 * @author sutton
 * @since 2024-05-19 15:14
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 2669293150219020239L;

    private String username;

    private String password;
}