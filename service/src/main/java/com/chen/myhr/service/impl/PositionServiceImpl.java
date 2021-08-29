package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.mapper.PositionMapper;
import com.chen.myhr.service.PositionService;
import com.chen.myhr.bean.Position;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Override
    public boolean checkPositionName(String name) {

        Integer count = baseMapper.selectCount(new QueryWrapper<Position>().eq("name", name));
        return count > 0;
    }
}
