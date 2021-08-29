package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.mapper.SalaryMapper;
import com.chen.myhr.service.SalaryService;
import com.chen.myhr.bean.Salary;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements SalaryService {

    @Override
    public boolean checkName(Salary salary) {

        QueryWrapper<Salary> salaryQueryWrapper = new QueryWrapper<>();
        // 排除进行修改时，对自身数据的查询
        if (!ObjectUtils.isEmpty(salary.getId())) {
            salaryQueryWrapper.ne("id", salary.getId());
        }
        // 判断名称不重名
        salaryQueryWrapper.eq("name", salary.getName());

        Integer count = baseMapper.selectCount(salaryQueryWrapper);
        return count > 0;
    }
}
