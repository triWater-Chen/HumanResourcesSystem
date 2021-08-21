package com.chen.myhr.common.exception;

/**
 * @author Chen
 * @description 自定义异常信息枚举类
 * @create 2021-08-19
 */
public enum MyExceptionCode {

    /**
     * 对应输出的提示信息
     */
    EXPORT_EXCEL_FAIL("导出Excel失败，请联系网站管理员！"),
    ERROR_FILE_NAME("文件名非法，禁止下载"),
    IMPORT_EXCEL_EMPTY("导入数据不能为空"),
    ;

    private String desc;

    MyExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
