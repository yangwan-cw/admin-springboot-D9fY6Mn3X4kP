package com.ioomex.ioomexadminspringboot.module.request;

import lombok.Data;

import java.io.Serializable;

/**
 * UserRegisterRequest
 *
 * @author sutton
 * @since 2024-05-19 15:00
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 2669293150219020249L;

    private String username;

    private String password;

    private String checkPassword;

}