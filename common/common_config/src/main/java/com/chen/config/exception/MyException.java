package com.chen.config.exception;

/**
 * @author Chen
 * @description 自定义异常类处理
 * @create 2021-08-19
 */
public class MyException extends RuntimeException  {

    private MyExceptionCode code;

    public MyException (MyExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public MyExceptionCode getCode() {
        return code;
    }

    public void setCode(MyExceptionCode code) {
        this.code = code;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
