package com.chen.myhr;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Chen
 * @description 测试
 * @create 2021-07-28
 */
public class MyTest {

    @Test
    public void test() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("Ab123.");
        System.out.println(encode);

    }
}
