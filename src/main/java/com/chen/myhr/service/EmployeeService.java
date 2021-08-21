package com.chen.myhr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.EmployeePageReq;

import java.util.List;

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

    /**
     * 按条件查询员工，不进行分页，专用于导出 excel
     * @param req 查询条件
     * @return List<Employee>
     */
    List<Employee> listWithoutPage(EmployeePageReq req);

    /**
     * 判断员工身份证是否重复
     * @param employee 员工信息
     * @return boolean
     */
    boolean checkEmployeeIdCard(Employee employee);

    /**
     * 添加员工
     * @param employee 员工信息
     * @return boolean
     */
    boolean addEmployee(Employee employee);

    /**
     * 修改员工
     * @param employee 员工信息
     * @return boolean
     */
    boolean updateEmployee(Employee employee);

    /**
     * Excel 导入员工设置
     * @param employees 员工
     * @param updateSupport 是否进行更新而非添加
     * @return String 信息
     */
    String importEmployees(List<Employee> employees, boolean updateSupport);
}
