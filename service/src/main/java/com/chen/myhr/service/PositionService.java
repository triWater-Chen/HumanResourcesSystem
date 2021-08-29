package com.chen.myhr.service;

import com.chen.myhr.bean.Position;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface PositionService extends IService<Position> {

    /**
     * 判断职位名称是否重复
     * @param name 职位名称
     * @return boolean
     */
    boolean checkPositionName(String name);
}
