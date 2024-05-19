package com.ioomex.ioomexadminspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ioomex.ioomexadminspringboot.mapper")
public class IoomexAdminSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoomexAdminSpringbootApplication.class, args);
    }

}
