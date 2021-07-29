package com.chen.myhr.config;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Chen
 * @description 配置 spring security
 * @create 2021-07-28
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    HrService hrService;

    /**
     * 使用该方法对密码进行加密
     * @return 加密后的密码
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 对登录用户进行配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    /**
     * 对登录拦截规则进行配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated() // 对所有的请求，登录之后就可访问
                .and()
                .formLogin() // 表单登录
                // 自动跳转的登录页面地址
                .loginPage("/login")
                .permitAll() // 登录相关的接口无需登录就可访问
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json; charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(Result.done().message("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll() // 登出相关的接口无需登录就可访问
                .and()
                .csrf().disable(); // 测试使用

        http.addFilterAt(authenticationJsonFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 注册功能，使 spring security 使用 JSON 提交的方法生效
     */
    @Bean
    AuthenticationJsonFilter authenticationJsonFilter() {

        AuthenticationJsonFilter filter = new AuthenticationJsonFilter();

        // 设置登录请求地址
        filter.setFilterProcessesUrl("/doLogin");

        // 处理登录成功
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                Hr hr = (Hr) authentication.getPrincipal(); // 得到登录的用户对象
                hr.setPassword(null);
                Result result = Result.done().message("登录成功").data("user", hr);
                out.write(new ObjectMapper().writeValueAsString(result)); // 将返回结果打印
                out.flush();
                out.close();
            }
        });

        // 处理登录失败
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                Result result = Result.error().message("登录失败");
                if (e instanceof LockedException) {
                    result.message("账户被锁定，请联系管理员");
                } else if (e instanceof CredentialsExpiredException) {
                    result.message("密码过期，请联系管理员");
                } else if (e instanceof AccountExpiredException) {
                    result.message("账户过期，请联系管理员");
                } else if (e instanceof DisabledException) {
                    result.message("账户被禁用，请联系管理员");
                } else if (e instanceof BadCredentialsException) {
                    result.message("用户名或密码错误，请重新输入");
                }
                out.write(new ObjectMapper().writeValueAsString(result));
                out.flush();
                out.close();
            }
        });

        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;

    }
}
