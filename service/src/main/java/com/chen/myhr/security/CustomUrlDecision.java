package com.chen.myhr.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Chen
 * @description 根据请求地址所需的角色，判断当前用户是否具备所需角色
 * @create 2021-07-30
 */
@Component
public class CustomUrlDecision implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        // 遍历 CustomFilter 中传回来的角色
        for (ConfigAttribute configAttribute : configAttributes) {

            String needRole = configAttribute.getAttribute();

            if ("ROLE_LOGIN".equals(needRole)) {
                // 如果返回的，是登录后就可访问的标记，则只需判断是否登录即可，未登录抛异常，登录则该方法结束

                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请先登录！");
                }
                return;
            }

            // 获取当前登录用户所具备的所有角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // 判断用户所具备的角色中是否具备 CustomFilter 传回来的角色
            for (GrantedAuthority authority : authorities) {

                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }

        // 若当前登录用户不具备所需要的角色
        throw new AccessDeniedException("权限不足，请联系管理员！");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
