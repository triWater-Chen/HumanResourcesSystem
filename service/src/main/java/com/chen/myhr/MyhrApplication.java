package com.chen.myhr;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Chen
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.chen"})
public class MyhrApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MyhrApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MyhrApplication.class, args);
        LOG.info("swagger 文档地址：http://localhost:7000/swagger-ui.html");
    }

}
