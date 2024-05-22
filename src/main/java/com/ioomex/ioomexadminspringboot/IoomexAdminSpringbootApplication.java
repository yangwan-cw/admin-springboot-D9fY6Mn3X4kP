package com.ioomex.ioomexadminspringboot;

import com.ioomex.ioomexadminspringboot.config.ApplicationRunStarter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author ioomex
 */
@SpringBootApplication
@MapperScan("com.ioomex.ioomexadminspringboot.mapper")
public class IoomexAdminSpringbootApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(IoomexAdminSpringbootApplication.class, args);
        Environment env = run.getEnvironment();
        ApplicationRunStarter.logApplicationStartup(env);
    }
}
