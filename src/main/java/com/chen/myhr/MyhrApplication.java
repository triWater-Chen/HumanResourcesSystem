package com.chen.myhr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Chen
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.chen"})
@MapperScan("com.chen.myhr.mapper")
public class MyhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrApplication.class, args);
    }

}
