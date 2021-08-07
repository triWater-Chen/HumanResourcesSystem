package com.chen.myhr.service;

import com.chen.myhr.bean.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.myhr.bean.vo.request.DepartmentReq;
import com.chen.myhr.bean.vo.result.DepartmentWithChildren;

import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取所有部门，返回部门树
     * @return List<DepartmentWithChildren>
     */
    List<DepartmentWithChildren> getDepartmentsTree();

    /**
     * 按条件查询部门
     * @param req 所查询的部门
     * @return List<DepartmentWithChildren>
     */
    List<Department> listByCondition(DepartmentReq req);
}
