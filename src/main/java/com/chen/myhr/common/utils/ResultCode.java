package com.chen.myhr.common.utils;

/**
 * @author Chen
 */
public interface ResultCode {

    /**
     * 请求成功
     */
    Integer SUCCESS = 200;

    /**
     * 请求资源不存在
     */
    Integer ERROR = 404;

    /**
     * 内部服务器错误
     */
    Integer SYSTEM_ERROR = 500;
}
