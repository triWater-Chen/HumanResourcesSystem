package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.MenuRole;
import com.chen.myhr.bean.Role;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.mapper.MenuRoleMapper;
import com.chen.myhr.service.MenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.myhr.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Resource
    RoleService roleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateMenuByRole(RoleUpdateReq req) {

        // new 一个 MenuRole 对象用于存放插入表中的数据
        int sum = 0;
        MenuRole menuRole = new MenuRole();

        // 先判断 req 是否有 id，无则进行添加，有则进行修改
        if (ObjectUtils.isEmpty(req.getId())) {
            // 进行添加

            // 通过 name 查出刚添加的 role 的 id
            Integer rid = roleService.getBaseMapper()
                    .selectOne(new QueryWrapper<Role>().eq("name", "ROLE_" + req.getName()))
                    .getId();
            menuRole.setRid(rid);
        } else {
            // 进行修改

            // 先根据角色 id，将原 id 绑定的菜单清空
            baseMapper.delete(new QueryWrapper<MenuRole>().eq("rid", req.getId()));
            menuRole.setRid(req.getId());
            // 若要批量插入， 在 xml 中：
            // <insert id="insertBatch">
            //    insert into menu_role (mid, rid) values
            //    <foreach collection="menuIds" item="mid" separator=",">
            //        (#{mid}, #{rid})
            //    </foreach>
            //</insert>
        }

        // 将所需菜单项遍历插入
        for (Integer mid : req.getMenuIds()) {
            // mybatisPlus 只能逐条插入

            menuRole.setMid(mid);
            int insert = baseMapper.insert(menuRole);
            sum += insert;
        }

        return sum == req.getMenuIds().size();
    }
}
