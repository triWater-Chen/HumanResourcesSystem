package com.chen.myhr.security;

import com.chen.myhr.bean.Menu;
import com.chen.myhr.service.MenuService;
import com.chen.myhr.bean.Role;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @author Chen
 * @description 根据用户传来的请求地址，返回所有有权访问该地址的角色
 * @create 2021-07-30
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Resource
    MenuService menuService;

    /**
     * 添加路径匹配规则类，进行路径匹配
     */
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        // 获取当前请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        // 获取每个菜单及其对应的角色
        List<Menu> menus = menuService.getMenusWithRole();

        for (Menu menu : menus) {

            // 对所有角色的路径进行匹配，获取匹配到的路径所对应的角色
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                List<Role> roles = menu.getRoles();

                String[] str = new String[roles.size()];
                // 遍历所需角色，将其存入 str数组中
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getName();
                }

                // 返回所有有权访问的角色
                return SecurityConfig.createList(str);
            }
        }

        // 若没有匹配上，则将其设置为登录后可访问
        // 通过设置标记 ”ROLE_LOGIN“ 来实现
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
