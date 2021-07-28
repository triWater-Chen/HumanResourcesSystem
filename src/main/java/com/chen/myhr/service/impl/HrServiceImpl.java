package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.mapper.HrMapper;
import com.chen.myhr.service.HrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class HrServiceImpl extends ServiceImpl<HrMapper, Hr> implements HrService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 按用户名查询用户
        QueryWrapper<Hr> hrQueryWrapper = new QueryWrapper<>();
        hrQueryWrapper.eq("username", username);
        Hr hr = baseMapper.selectOne(hrQueryWrapper);

        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        return hr;
    }
}
