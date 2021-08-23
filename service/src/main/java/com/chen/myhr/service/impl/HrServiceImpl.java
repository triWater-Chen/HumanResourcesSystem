package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.HrRole;
import com.chen.myhr.bean.vo.request.HrReq;
import com.chen.myhr.service.HrRoleService;
import com.chen.myhr.service.HrService;
import com.chen.myhr.service.RoleService;
import com.chen.myhr.bean.Hr;
import com.chen.myhr.bean.Role;
import com.chen.myhr.mapper.HrMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        // 按用户名或手机号查询用户
        QueryWrapper<Hr> hrQueryWrapper = new QueryWrapper<>();
        hrQueryWrapper.eq("username", username).or().eq("phone", username);
        Hr hr = baseMapper.selectOne(hrQueryWrapper);

        if (hr == null) {
            throw new UsernameNotFoundException("该账号不存在!");
        }

        // ----- 若登录成功，则设置该用户所具备的角色 -----
            // 1. 先通过 hr 的 id，查出与 hr_role 表中 hrid 相应数据
            QueryWrapper<HrRole> hrRoleQueryWrapper = new QueryWrapper<>();
            hrRoleQueryWrapper.eq("hrid", hr.getId());
            List<HrRole> hrRoles = hrRoleService.list(hrRoleQueryWrapper);
            // 若该用户下不具备角色，则不允许登录
            if (ObjectUtils.isEmpty(hrRoles)) {
                throw new InternalAuthenticationServiceException("该用户下无角色，请联系管理员");
            }

            // 2. 遍历上步得出的数据，通过获取其中的 rid
            List<Integer> idList = new ArrayList<>();
            for (HrRole hrRole : hrRoles) {
                idList.add(hrRole.getRid());
            }
            // 3.查出与 role 表中 id 相应数据，且该角色处于可用状态
            List<Role> roles = roleService.getBaseMapper()
                    .selectList(new QueryWrapper<Role>().in("id", idList).eq("enabled", true));
            // 若该用户下不具备角色，则不允许登录
            if (ObjectUtils.isEmpty(roles)) {
                throw new InternalAuthenticationServiceException("该用户下无可用角色，请联系管理员");
            }

            // 3.将上步得到的 role 表中数据插入 hr 表中 roles 字段
            hr.setRoles(roles);

        return hr;
    }

    @Override
    public boolean checkHrUsernameAndPhone(Hr hr) {

        QueryWrapper<Hr> hrQueryWrapper = new QueryWrapper<>();
        // 排除进行修改时，对自身原本数据的查询
        if (!ObjectUtils.isEmpty(hr.getId())) {
            hrQueryWrapper.ne("id", hr.getId());
        }

        // 判断用户名和电话都不重名
        hrQueryWrapper.and(i ->
                i.eq("username", hr.getUsername()).or().eq("phone", hr.getPhone()));
        /*
          .eq("username", hr.getUsername()).or().eq("phone", hr.getPhone()) 是错误的
          因为配合上面的 .ne，实际效果会是：(.ne.and.eq).or(.eq)，需要的是 .ne.and(.eq.or.eq)
         */

        Integer count = baseMapper.selectCount(hrQueryWrapper);
        return count > 0;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeHr(Integer id) {

        // 先删除该用户与角色的关联
        hrRoleService.remove(new QueryWrapper<HrRole>().eq("hrid", id));

        // 最后删除该用户
        int delete = baseMapper.deleteById(id);
        return delete > 0;
    }

    @Override
    public List<Role> getHrWithRole(Integer id) {

        // 先通过 hr 的 id，查出与 hr_role 表中 hrid 相应数据
        List<HrRole> hrRoles = hrRoleService.list(new QueryWrapper<HrRole>().eq("hrid", id));

        // 如果 hr 没有对应的角色，直接返回空数组
        if (ObjectUtils.isEmpty(hrRoles)) {
            return new ArrayList<>();
        }

        List<Integer> idList = new ArrayList<>();
        // 遍历 hrRoles 数据，通过整合其中的 rid， 查询与 role 表中 id 相应数据
        for (HrRole hrRole : hrRoles) {
            idList.add(hrRole.getRid());
        }
        return roleService.listByIds(idList);
    }
}
