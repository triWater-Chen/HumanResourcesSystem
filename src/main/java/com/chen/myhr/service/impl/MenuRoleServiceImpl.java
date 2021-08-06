package com.chen.myhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.myhr.bean.MenuRole;
import com.chen.myhr.bean.vo.request.RoleUpdateReq;
import com.chen.myhr.mapper.MenuRoleMapper;
import com.chen.myhr.service.MenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author Chen
 * @since 2021-07-28
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMenuByRole(RoleUpdateReq req) {

        // 先根据角色 id，将原 id 绑定的菜单清空
        baseMapper.delete(new QueryWrapper<MenuRole>().eq("rid", req.getId()));

        // 插入所修改的菜单
        int sum = 0;
        MenuRole menuRole = new MenuRole();
        menuRole.setRid(req.getId());
        for (Integer mid : req.getMenuIds()) {
            // mybatisPlus 只能逐条插入

            menuRole.setMid(mid);
            int insert = baseMapper.insert(menuRole);
            sum += insert;
        }

        return sum == req.getMenuIds().size();
        // 若要批量插入， 在 xml 中：
        // <insert id="insertBatch">
        //    insert into menu_role (mid, rid) values
        //    <foreach collection="menuIds" item="mid" separator=",">
        //        (#{mid}, #{rid})
        //    </foreach>
        //</insert>
    }
}
