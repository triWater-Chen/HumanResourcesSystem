package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.HrRole;
import com.chen.myhr.bean.vo.request.HrUpdateReq;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.mapper.HrRoleMapper;
import com.chen.myhr.service.HrRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class HrRoleServiceImpl extends ServiceImpl<HrRoleMapper, HrRole> implements HrRoleService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean hrUpdateByRole(HrUpdateReq req) {

        // new 一个 HrRole 对象用于存放插入表中的数据
        int sum = 0;
        HrRole hrRole = new HrRole();

        if (ObjectUtils.isEmpty(req.getId())) {
            return false;
        } else {

            // 先根据用户 id，将原 id 绑定的角色清空
            baseMapper.delete(new QueryWrapper<HrRole>().eq("hrid", req.getId()));
            // 将被修改的用户 id 复制到新对象上
            hrRole.setHrid(req.getId());
            // 将所需角色项遍历插入
            for (Integer rid : req.getRoleIds()) {

                hrRole.setRid(rid);
                int insert = baseMapper.insert(hrRole);
                sum += insert;
            }
            return sum == req.getRoleIds().size();
        }
    }
}
