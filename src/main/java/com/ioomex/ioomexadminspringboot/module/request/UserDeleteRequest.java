package com.ioomex.ioomexadminspringboot.module.request;

import lombok.Data;

import java.util.List;

/**
 * UserDeleteRequest
 *
 * @author sutton
 * @since 2024-05-19 16:43
 */
@Data
public class UserDeleteRequest {

    private Integer id;

    private List<Integer> ids;
}