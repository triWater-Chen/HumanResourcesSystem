package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Department;
import com.chen.myhr.bean.Employee;
import com.chen.myhr.bean.vo.request.DepartmentReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.myhr.common.utils.CommonConstants;
import com.chen.myhr.common.utils.CopyUtil;
import com.chen.myhr.mapper.DepartmentMapper;
import com.chen.myhr.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.myhr.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    EmployeeService employeeService;

    @Override
    public boolean checkDepartmentName(String name) {

        Integer count = baseMapper.selectCount(new QueryWrapper<Department>().eq("name", name));
        return count > 0;
    }

    @Override
    public List<DepartmentWithChildren> getDepartmentsTree() {

        // 先得到一级部门（因为要返回 List，所以此处要查 List<> 格式）
        List<Department> departments =
                baseMapper.selectList(new QueryWrapper<Department>().eq("parentId", -1));
        // 对得到的部门进行封装
        List<DepartmentWithChildren> departmentList = CopyUtil.copyList(departments, DepartmentWithChildren.class);

        // 遍历查询子部门（因为一级部门就一个，直接 get(0) 即可）
        if (!departments.isEmpty()) {
            findAllChild(departmentList.get(0));
        }

        return departmentList;
    }

    @Override
    public List<Department> listByCondition(DepartmentReq req) {

        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();

        // 动态 sql 查询
        if (StringUtils.hasLength(req.getName())) {
            departmentQueryWrapper.like("name", req.getName());
        }
        if (StringUtils.hasLength(req.getBeginTime())) {
            departmentQueryWrapper.ge("createDate", req.getBeginTime() + " 00:00:00");
        }
        if (!ObjectUtils.isEmpty(req.getEnabled())) {
            departmentQueryWrapper.eq("enabled", req.getEnabled());
        }
        if (StringUtils.hasLength(req.getEndTime())) {
            departmentQueryWrapper.le("createDate", req.getEndTime() + " 23:59:59");
        }
        departmentQueryWrapper.orderByAsc("id");

        return baseMapper.selectList(departmentQueryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateDepartment(Department req) {

        // 修改状态，实现：若节点禁用，其子节点需全是禁用状态；若节点可用，其父节点必可用
        if (req.getEnabled()) {
            // 若节点可用，其父节点必可用（只设置上一级的父节点）

            // 查询其父节点，并将父节点设置为可用（只设置上一级的父节点）
            Department departmentPapa = baseMapper.selectById(req.getParentId());
            departmentPapa.setEnabled(true);
            baseMapper.updateById(departmentPapa);
        } else {
            // 若节点禁用，其子节点需全是禁用状态（只需判断下一级节点全为禁用即可）

            // 查询子部门
            List<Department> departmentChild
                    = baseMapper.selectList(new QueryWrapper<Department>().eq("parentId", req.getId()));
            // 若存在子部门，将下一级子部门进行遍历
            if (!CollectionUtils.isEmpty(departmentChild)) {
                for (Department department : departmentChild) {

                    // 若子部门中有可用的部门，直接返回
                    if (department.getEnabled()) {
                        return CommonConstants.STATUS_A;
                    }
                }
            }
        }

        if (baseMapper.updateById(req) > 0) {
            return CommonConstants.SQL_SUCCESS;
        } else {
            return CommonConstants.SQL_ERROR;
        }
    }

    @Override
    public String removeDepartment(Integer id) {

        // 若存在下级部门，删除失败（与判断节点禁用相同，只需判断是否存在下一级节点）
        Integer countDep =
                baseMapper.selectCount(new QueryWrapper<Department>().eq("parentId", id));
        if (countDep > 0) {
            return CommonConstants.STATUS_A;
        }

        // 查询员工表，若该部门中仍有成员，删除失败
        Integer countEmp =
                employeeService.getBaseMapper()
                        .selectCount(new QueryWrapper<Employee>().eq("departmentId", id));
        if (countEmp > 0) {
            return CommonConstants.STATUS_B;
        }

        if (baseMapper.deleteById(id) > 0) {
            return CommonConstants.SQL_SUCCESS;
        } else {
            return CommonConstants.SQL_ERROR;
        }
    }

    /**
     * 实现递归查询
     */
    public void findAllChild(DepartmentWithChildren departmentList) {

        // 查出下一级部门
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("parentId", departmentList.getId())
                .orderByAsc("sort");
        List<Department> departments = baseMapper.selectList(departmentQueryWrapper);

        // 对得到的部门进行封装
        List<DepartmentWithChildren> departmentChildren = CopyUtil.copyList(departments, DepartmentWithChildren.class);

        // 递归查询子部门
        if (!departments.isEmpty()) {
            // 若非空，则将得到的部门放进父部门的 list 中，再进行递归查询

            departmentList.setChildren(departmentChildren);
            departmentChildren.forEach(this::findAllChild);
        }

    }
}
