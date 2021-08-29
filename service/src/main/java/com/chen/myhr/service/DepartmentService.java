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
     * 判断部门名称是否重复
     * @param name 部门名称
     * @return boolean
     */
    boolean checkDepartmentName(String name);

    /**
     * 获取所有部门，返回部门树
     * @return List<DepartmentWithChildren>
     */
    List<DepartmentWithChildren> getDepartmentsTree();

    /**
     * 按条件查询部门
     * @param req 所查询的部门
     * @return List<Department>
     */
    List<Department> listByCondition(DepartmentReq req);

    /**
     * 修改部门
     * @param req 部门参数
     * @return String
     */
    String updateDepartment(Department req);

    /**
     * 删除部门
     * @param id 部门 id
     * @return String
     */
    String removeDepartment(Integer id);
}
