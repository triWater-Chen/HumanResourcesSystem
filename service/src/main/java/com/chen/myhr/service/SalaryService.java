package com.chen.myhr.service;

import com.chen.myhr.bean.Salary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface SalaryService extends IService<Salary> {

    /**
     * 检查工资账套名称是否唯一
     * @param salary 工资账套详情
     * @return boolean
     */
    boolean checkName(Salary salary);
}
