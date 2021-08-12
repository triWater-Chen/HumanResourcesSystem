package com.chen.myhr.config;

import com.chen.myhr.bean.Hr;
import com.chen.myhr.common.utils.Result;
import com.chen.myhr.service.HrService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
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

    @Resource
    CustomFilter customFilter;

    @Resource
    CustomUrlDecision customUrlDecision;

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
//                .anyRequest().authenticated() // 对所有的请求，登录之后就可访问
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {

                        o.setAccessDecisionManager(customUrlDecision);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                }) // 对自定义的过滤器注入，对所有请求进行过滤
                .and()
                .formLogin() // 表单登录
                // 自动跳转的登录页面地址 .loginPage("/login")
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
                .csrf().disable()
                // 没有认证时，在此处理结果，返回 json 数据，不需要重定向到 /login
                // 解决前端没登录时路径乱输入会跳转到 localhost:7000/login 页面，报错跨域（因为路径不对时前端直接访问该地址，没经过 node.js 代理）
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json; charset=UTF-8");
                        response.setStatus(401);
                        PrintWriter out = response.getWriter();
                        Result result = Result.error().message("访问失败");
                        if (e instanceof InsufficientAuthenticationException) {
                            result.message("非法请求");
                        }
                        out.write(new ObjectMapper().writeValueAsString(result));
                        out.flush();
                        out.close();
                    }
                });

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
                    result.message("账号或密码错误，请重新输入");
                } else if (e instanceof InternalAuthenticationServiceException) {
                    result.message("该用户下无角色，无法登录");
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

    /**
     * 解决 swagger2 被拦截问题
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // allow Swagger URL to be accessed without authentication
        web.ignoring().antMatchers(
                "/swagger-ui.html",
                // swagger api json
                "/v2/api-docs",
                // 用来获取支持的动作
                "/swagger-resources/configuration/ui",
                // 用来获取api-docs的URI
                "/swagger-resources",
                // 安全选项
                "/swagger-resources/configuration/security",
                "/swagger-resources/**",
                //补充路径，近期在搭建swagger接口文档时，通过浏览器控制台发现该/webjars路径下的文件被拦截，故加上此过滤条件即可。(2020-10-23)
                "/webjars/**"


        );
    }
}
