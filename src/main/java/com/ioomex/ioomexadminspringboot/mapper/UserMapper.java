package com.ioomex.ioomexadminspringboot.mapper;

import com.ioomex.ioomexadminspringboot.module.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-05-18 16:32:38
* @Entity com.ioomex.ioomexadminspringboot.module.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




