package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Department;
import com.chen.myhr.bean.vo.request.DepartmentReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;
import com.chen.myhr.common.utils.CopyUtil;
import com.chen.myhr.mapper.DepartmentMapper;
import com.chen.myhr.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<DepartmentWithChildren> getDepartmentsTree() {

        // 先得到一级部门
        List<Department> departments = baseMapper.selectList(new QueryWrapper<Department>().eq("parentId", -1));
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
