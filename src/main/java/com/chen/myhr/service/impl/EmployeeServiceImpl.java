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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

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
        employeeQueryWrapper.orderByDesc("id");

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

    @Override
    public boolean checkEmployeeIdCard(Employee employee) {

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        // 排除进行修改时，对自身数据的查询
        if (!ObjectUtils.isEmpty(employee.getId())) {
            employeeQueryWrapper.ne("id", employee.getId());
        }
        // 判断身份证号不重名
        employeeQueryWrapper.eq("idCard", employee.getIdCard());

        Integer count = baseMapper.selectCount(employeeQueryWrapper);
        return count > 0;
    }

    @Override
    public boolean addEmployee(Employee employee) {

        // 计算合同期限
        calculationContract(employee);

        // 计算工号：取出当前最大工号并 +1
        employee.setWorkId(String.format("%08d", baseMapper.maxWorkId() + 1));

        int insert = baseMapper.insert(employee);
        return insert > 0;
    }

    @Override
    public boolean updateEmployee(Employee employee) {

        // 计算合同期限
        calculationContract(employee);

        int update = baseMapper.updateById(employee);
        return update > 0;
    }

    /**
     * 计算合同期限
     * @param employee 员工信息
     */
    private void calculationContract(Employee employee) {

        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12
                + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month / 12)));
    }
}
