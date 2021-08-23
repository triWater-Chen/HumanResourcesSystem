package com.chen.config.result;

/**
 * @author Chen
 */
public interface ResultCode {

    /**
     * 请求成功
     */
    Integer SUCCESS = 200;

    /**
     * 内部服务器错误
     */
    Integer ERROR = 500;

    /**
     * 请求资源不存在
     */
    Integer RESOURCES_ERROR = 404;


}
