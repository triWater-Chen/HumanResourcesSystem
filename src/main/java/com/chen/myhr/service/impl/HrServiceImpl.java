package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.HrRole;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.mapper.HrMapper;
import com.chen.myhr.service.HrRoleService;
import com.chen.myhr.service.HrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.myhr.service.RoleService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class HrServiceImpl extends ServiceImpl<HrMapper, Hr> implements HrService {

    @Resource
    RoleService roleService;

    @Resource
    HrRoleService hrRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 按用户名查询用户
        QueryWrapper<Hr> hrQueryWrapper = new QueryWrapper<>();
        hrQueryWrapper.eq("username", username);
        Hr hr = baseMapper.selectOne(hrQueryWrapper);

        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }

        // ----- 若登录成功，则设置该用户所具备的角色 -----
            // 1. 先通过 hr 的 id，查出与 hr_role 表中 hrid 相应数据
            QueryWrapper<HrRole> hrRoleQueryWrapper = new QueryWrapper<>();
            hrRoleQueryWrapper.eq("hrid", hr.getId());
            List<HrRole> hrRoles = hrRoleService.list(hrRoleQueryWrapper);

            // 2. 遍历上步得出的数据，通过获取其中的 rid，查出与 role 表中 id 相应数据
            List<Integer> idList = new ArrayList<>();
            for (HrRole hrRole : hrRoles) {
                idList.add(hrRole.getRid());
            }
            List<Role> roles = roleService.listByIds(idList);

            // 3.将上步得到的 role 表中数据插入 hr 表中 roles 字段
            hr.setRoles(roles);

        return hr;
    }

    @Override
    public List<Hr> listHrByCondition(HrReq req) {

        QueryWrapper<Hr> hrQueryWrapper = new QueryWrapper<>();

        // 动态 sql 查询
        if (StringUtils.hasLength(req.getName())) {
            hrQueryWrapper.like("name", req.getName());
        }
        if (StringUtils.hasLength(req.getUsername())) {
            hrQueryWrapper.like("username", req.getUsername());
        }
        if (!ObjectUtils.isEmpty(req.getEnabled())) {
            hrQueryWrapper.eq("enabled", req.getEnabled());
        }
        if (StringUtils.hasLength(req.getBeginTime())) {
            hrQueryWrapper.ge("createDate", req.getBeginTime() + " 00:00:00");
        }
        if (StringUtils.hasLength(req.getEndTime())) {
            hrQueryWrapper.le("name", req.getEndTime() + " 23:59:59");
        }
        hrQueryWrapper.orderByAsc("id");

        // 将查询到的结果排除 password 字段
        // i -> !"password".equals(i.getColumn()) 也可以实现
        hrQueryWrapper.select(Hr.class, i -> !"password".equals(i.getProperty()));

        return baseMapper.selectList(hrQueryWrapper);
    }
}
