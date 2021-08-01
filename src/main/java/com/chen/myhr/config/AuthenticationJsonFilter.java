package com.chen.myhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Chen
 * @description 使 spring security 能接受 JSON 格式的提交
 * @create 2021-07-29
 */
public class AuthenticationJsonFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 先判断是否是 POST 请求
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 处理以 JSON 形式传递的参数
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {

            String username = null;
            String password = null;

            try {
                Map map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                username = String.valueOf(map.get("username"));
                password = String.valueOf(map.get("password"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            username = username != null ? username : "";
            username = username.trim();
            password = password != null ? password : "";
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        }

        return super.attemptAuthentication(request, response);
    }
}
