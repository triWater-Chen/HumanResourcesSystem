package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.*;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.mapper.EmployeeMapper;
import com.chen.myhr.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    JoblevelService joblevelService;

    @Resource
    PositionService positionService;

    @Resource
    DepartmentService departmentService;

    @Resource
    NationService nationService;

    @Resource
    PoliticsstatusService politicsstatusService;

    @Override
    public Page<Employee> listByCondition(EmployeePageReq req) {

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        Page<Employee> page = new Page<>(req.getCurrent(), req.getSize());

        // 动态 sql 查询
        if (StringUtils.hasLength(req.getName())) {
            employeeQueryWrapper.like("name", req.getName());
        }
        if (StringUtils.hasLength(req.getIdCard())) {
            employeeQueryWrapper.like("idCard", req.getIdCard());
        }
        if (StringUtils.hasLength(req.getWorkId())) {
            employeeQueryWrapper.like("workId", req.getWorkId());
        }
        if (!ObjectUtils.isEmpty(req.getJobLevelId())) {
            employeeQueryWrapper.like("jobLeveId", req.getJobLevelId());
        }
        if (!ObjectUtils.isEmpty(req.getPosId())) {
            employeeQueryWrapper.like("posId", req.getPosId());
        }
        if (!ObjectUtils.isEmpty(req.getDepartmentId())) {
            employeeQueryWrapper.like("departmentId", req.getDepartmentId());
        }
        if (StringUtils.hasLength(req.getBeginTime())) {
            employeeQueryWrapper.like("beginDate", req.getBeginTime());
        }
        if (StringUtils.hasLength(req.getEndTime())) {
            employeeQueryWrapper.like("beginDate", req.getEndTime());
        }
        employeeQueryWrapper.orderByAsc("id");

        Page<Employee> employeePage = baseMapper.selectPage(page, employeeQueryWrapper);
        List<Employee> employees = employeePage.getRecords();

        // 通过遍历，将相关联的数据整合进去
        for (Employee employee : employees) {

            employee.setJobLevel(joblevelService.getBaseMapper().selectOne(
                    new QueryWrapper<Joblevel>().select("id", "name").eq("id", employee.getJobLevelId())
            ));
            employee.setPosition(positionService.getBaseMapper().selectOne(
                    new QueryWrapper<Position>().select("id", "name").eq("id", employee.getPosId())
            ));
            employee.setDepartment(departmentService.getBaseMapper().selectOne(
                    new QueryWrapper<Department>().select("id", "name").eq("id", employee.getDepartmentId())
            ));
            employee.setNation(nationService.getById(employee.getNationId()));
            employee.setPoliticsStatus(politicsstatusService.getById(employee.getPoliticId()));
        }

        // 将最终值传入分页数据中
        return employeePage.setRecords(employees);
    }
}
