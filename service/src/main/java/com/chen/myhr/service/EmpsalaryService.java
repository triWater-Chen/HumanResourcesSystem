package com.chen.myhr.service;

import com.chen.myhr.bean.Empsalary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.bean.vo.result.EmployeeWithSalaryPage;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface EmpsalaryService extends IService<Empsalary> {

    /**
     * 按条件查询员工账套
     * @param req 查询条件
     * @return EmployeeWithSalaryPage
     */
    EmployeeWithSalaryPage getEmployeeList(EmployeePageReq req);

    /**
     * 修改员工工资账套
     * @param empsalary 修改信息
     * @return boolean
     */
    boolean updateEmpSalary(Empsalary empsalary);
}
