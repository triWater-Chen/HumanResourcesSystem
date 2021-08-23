package com.chen.myhr.service;

import com.chen.myhr.bean.Joblevel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface JoblevelService extends IService<Joblevel> {

    /**
     * 判断职称名称是否重复
     * @param name 职称名称
     * @return boolean
     */
    boolean checkJobLevelName(String name);
}
