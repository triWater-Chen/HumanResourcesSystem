package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Joblevel;
import com.chen.myhr.service.JoblevelService;
import com.chen.myhr.mapper.JoblevelMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class JoblevelServiceImpl extends ServiceImpl<JoblevelMapper, Joblevel> implements JoblevelService {

    @Override
    public boolean checkJobLevelName(String name) {

        Integer count = baseMapper.selectCount(new QueryWrapper<Joblevel>().eq("name", name));
        return count > 0;
    }
}
