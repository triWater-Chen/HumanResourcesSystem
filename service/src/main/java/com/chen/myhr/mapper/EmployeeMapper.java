package com.chen.myhr.mapper;

import com.chen.myhr.bean.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Chen
 * @since 2021-07-28
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询最大的工号
     * @return Integer
     */
    @Select("SELECT max(workID) FROM employee")
    Integer maxWorkId();
}
