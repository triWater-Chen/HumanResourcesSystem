package com.chen.myhr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.EmployeePageReq;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 按条件分页查询员工
     * @param req 查询条件
     * @return Page<Employee>
     */
    Page<Employee> listByCondition(EmployeePageReq req);
}
