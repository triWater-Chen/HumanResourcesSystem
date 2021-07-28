package com.chen.myhr.service.impl;

import com.chen.myhr.bean.Employee;
import com.chen.myhr.mapper.EmployeeMapper;
import com.chen.myhr.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
