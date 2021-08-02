package com.chen.myhr.common.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author Chen
 * @description 实现 mybatisPlus 自动填充
 * @create 2021-08-02
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        // 此处为属性名，非数据表中字段名
        // 官网使用的是 LocalDateTime，但该项目中对应字段类型是 Date，所以用 Date.class
        this.strictInsertFill(metaObject, "createdate", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}