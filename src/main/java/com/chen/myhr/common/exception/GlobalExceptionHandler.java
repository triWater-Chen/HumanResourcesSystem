package com.chen.myhr.common.exception;

import com.chen.myhr.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Chen
 * @description 统一对异常进行处理
 * @create 2021-08-02
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // RestControllerAdvice 相当于 @ControllerAdvice + @ResponseBody

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 统一处理格式不符合数据库要求的异常
     * @return Result
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public Result validExceptionHandler(SQLIntegrityConstraintViolationException e) {
        LOG.warn("数据库异常：{}", e.getMessage());

        // 处理添加时，字段重名异常
        String duplicate = "Duplicate";
        // 处理删除时，因该字段有关联数据异常
        String foreign = "foreign";

        if (e.getMessage().contains(duplicate)) {
            return Result.error().message("添加失败，不允许重名");
        } else if (e.getMessage().contains(foreign)) {
            return Result.error().message("该数据与其他数据相关联，无法删除");
        } else {
            return Result.error().message(e.getMessage());
        }
    }
}
