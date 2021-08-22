package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.myhr.bean.*;
import com.chen.myhr.bean.vo.request.EmployeePageReq;
import com.chen.myhr.common.exception.MyException;
import com.chen.myhr.common.exception.MyExceptionCode;
import com.chen.myhr.mapper.EmployeeMapper;
import com.chen.myhr.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

        Page<Employee> page = new Page<>(req.getCurrent(), req.getSize());
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();

        // 动态 sql 查询
        sqlByCondition(employeeQueryWrapper, req);

        Page<Employee> employeePage = baseMapper.selectPage(page, employeeQueryWrapper);
        List<Employee> employees = employeePage.getRecords();

        // 通过遍历，将相关联的数据整合进去
        setEmployeeDetail(employees);

        // 将最终值传入分页数据中
        return employeePage.setRecords(employees);
    }

    @Override
    public List<Employee> listWithoutPage(EmployeePageReq req) {

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        sqlByCondition(employeeQueryWrapper, req);

        List<Employee> employees = baseMapper.selectList(employeeQueryWrapper);
        setEmployeeDetail(employees);

        return employees;
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

    @Override
    public String importEmployees(List<Employee> employees, boolean updateSupport) {

        if (CollectionUtils.isEmpty(employees)) {
            throw new MyException(MyExceptionCode.IMPORT_EXCEL_EMPTY);
        }
        int successCount = 0;
        int failureCount = 0;
        StringBuilder successMessage = new StringBuilder();
        StringBuilder failureMessage = new StringBuilder();

        for (Employee employee : employees) {
            try {
                // 用于判断添加时 身份证号 是否唯一
                Integer count = baseMapper.selectCount(new QueryWrapper<Employee>().eq("idCard", employee.getIdCard()));

                // 用于判断更新时 身份证号 是否唯一
                // 判断添加分开不使用原因在于：employee 带有 id，添加需要 id 为空，会导致错误
                boolean updateEnabled = checkEmployeeIdCard(employee);

                if (count == 0 && !updateSupport) {
                    // 进行添加

                    employee.setId(null);
                    addEmployee(employee);
                    successCount++;
                    successMessage.append("<br/>")
                            .append(successCount)
                            .append("、员工 ")
                            .append(employee.getName())
                            .append(" 导入成功");
                } else if (updateSupport && !updateEnabled) {
                    // 进行更新

                    updateEmployee(employee);
                    successCount++;
                    successMessage.append("<br/>")
                            .append(successCount)
                            .append("、员工 ")
                            .append(employee.getName())
                            .append(" 更新成功");
                } else {

                    failureCount++;
                    failureMessage.append("<br/>")
                            .append(failureCount)
                            .append("、员工 ")
                            .append(employee.getName())
                            .append(" 的身份证号：")
                            .append(employee.getIdCard())
                            .append(" 已存在");
                }
            } catch (Exception e) {

                failureCount++;
                String msg = "<br/>" + failureCount + "、员工 " + employee.getName() + " 导入失败：";
                failureMessage.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureCount > 0) {
            failureMessage.insert(0, "很抱歉，存在导入失败！共 " + failureCount + " 条数据格式不正确，错误如下：");
            return failureMessage.toString();
        } else {
            successMessage.insert(0, "数据已全部导入成功！共" + successCount + " 条，数据如下：");
            return successMessage.toString();
        }
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

    /**
     * 提取 设置员工关联信息 公共语句
     * @param employees List<Employee>
     */
    private void setEmployeeDetail(List<Employee> employees) {
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
    }

    /**
     * 提取 动态 sql 查询公共语句
     * @param employeeQueryWrapper queryWrapper
     * @param req EmployeePageReq
     */
    private void sqlByCondition(QueryWrapper<Employee> employeeQueryWrapper, EmployeePageReq req) {

        // 动态 sql 查询
        if (StringUtils.hasLength(req.getName())) {
            employeeQueryWrapper.like("name", req.getName());
        }
        if (StringUtils.hasLength(req.getIdCard())) {
            employeeQueryWrapper.eq("idCard", req.getIdCard());
        }
        if (StringUtils.hasLength(req.getWorkId())) {
            employeeQueryWrapper.like("workId", req.getWorkId());
        }
        if (!ObjectUtils.isEmpty(req.getJobLevelId())) {
            employeeQueryWrapper.eq("jobLeveId", req.getJobLevelId());
        }
        if (!ObjectUtils.isEmpty(req.getPosId())) {
            employeeQueryWrapper.eq("posId", req.getPosId());
        }
        if (!ObjectUtils.isEmpty(req.getDepartmentId())) {
            employeeQueryWrapper.eq("departmentId", req.getDepartmentId());
        }
        if (!ObjectUtils.isEmpty(req.getNationId())) {
            employeeQueryWrapper.eq("nationId", req.getNationId());
        }
        if (!ObjectUtils.isEmpty(req.getPoliticId())) {
            employeeQueryWrapper.eq("politicId", req.getPoliticId());
        }
        if (StringUtils.hasLength(req.getEngageForm())) {
            employeeQueryWrapper.eq("engageForm", req.getEngageForm());
        }
        if (StringUtils.hasLength(req.getWorkState())) {
            employeeQueryWrapper.eq("workState", req.getWorkState());
        }
        if (StringUtils.hasLength(req.getBeginTime())) {
            employeeQueryWrapper.ge("beginDate", req.getBeginTime());
        }
        if (StringUtils.hasLength(req.getEndTime())) {
            employeeQueryWrapper.le("beginDate", req.getEndTime());
        }
        employeeQueryWrapper.orderByDesc("id");
    }
}
