package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.Department;
import com.chen.myhr.bean.Employee;
import com.chen.myhr.bean.Salary;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.bean.vo.result.EmployeeWithSalary;
import com.chen.myhr.bean.vo.result.EmployeeWithSalaryPage;
import com.chen.myhr.mapper.EmpsalaryMapper;
import com.chen.myhr.service.DepartmentService;
import com.chen.myhr.service.EmployeeService;
import com.chen.myhr.service.EmpsalaryService;
import com.chen.myhr.bean.Empsalary;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.myhr.service.SalaryService;
import com.chen.utils.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class EmpsalaryServiceImpl extends ServiceImpl<EmpsalaryMapper, Empsalary> implements EmpsalaryService {

    @Resource
    EmployeeService employeeService;

    @Resource
    DepartmentService departmentService;

    @Resource
    SalaryService salaryService;

    @Override
    public EmployeeWithSalaryPage getEmployeeList(EmployeePageReq req) {

        EmployeeWithSalaryPage employeeWithSalaryPage = new EmployeeWithSalaryPage();

        // ----- 先查询并设置员工基本信息 -----
        Page<Employee> page = new Page<>(req.getCurrent(), req.getSize());
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();

            // 动态 sql 查询
            if (StringUtils.hasLength(req.getName())) {
                employeeQueryWrapper.like("name", req.getName());
            }
            if (StringUtils.hasLength(req.getWorkId())) {
                employeeQueryWrapper.like("workId", req.getWorkId());
            }
            if (!ObjectUtils.isEmpty(req.getDepartmentId())) {
                employeeQueryWrapper.eq("departmentId", req.getDepartmentId());
            }
            employeeQueryWrapper.orderByDesc("id");

            // 设置总页数
            Page<Employee> employeePage = employeeService.page(page, employeeQueryWrapper);
            employeeWithSalaryPage.setTotal(employeePage.getTotal());

            List<Employee> employees = employeePage.getRecords();
            // 通过遍历，将部门数据整合进去
            for (Employee employee : employees) {

                employee.setDepartment(departmentService.getBaseMapper().selectOne(
                        new QueryWrapper<Department>().select("id", "name").eq("id", employee.getDepartmentId())
                ));
            }

            List<EmployeeWithSalary> employeeWithSalaries = CopyUtil.copyList(employees, EmployeeWithSalary.class);
            // 此时的 employeeWithSalaries 为仅包含部门信息的员工列表

        // ----- 遍历设置进员工账套信息 -----
        for (EmployeeWithSalary employeeWithSalary : employeeWithSalaries) {

            Empsalary empsalary = baseMapper.selectOne(new QueryWrapper<Empsalary>().eq("eid", employeeWithSalary.getId()));

            if (!ObjectUtils.isEmpty(empsalary)) {
                employeeWithSalary.setSalary(salaryService.getBaseMapper().selectOne(
                        new QueryWrapper<Salary>().eq("id", empsalary.getSid())
                ));
            }
        }
        employeeWithSalaryPage.setList(employeeWithSalaries);

        return employeeWithSalaryPage;
    }

    @Override
    public boolean updateEmpSalary(Empsalary empsalary) {

        Integer count = baseMapper.selectCount(new QueryWrapper<Empsalary>().eq("eid", empsalary.getEid()));
        int result;
        if (count > 0) {
            // 若关联表中员工已存在，则进行更新

            result = baseMapper.update(empsalary, new UpdateWrapper<Empsalary>().eq("eid", empsalary.getEid()));
        } else {
            // 否则进行添加

            result = baseMapper.insert(empsalary);
        }
        return result > 0;
    }
}
