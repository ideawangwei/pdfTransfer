package com.bonc.pdf.exception;

/**
 * Created by ldf on 2018/2/28.
 */
public enum SysErrorEnums implements IErrorCode {

    CHECK_PAGE_NUM_WRONGFUL("C10001","页码不合法，页码不能大于最大页数，不能小于1");
    private String errorCode;
    private String errorMessage;
    SysErrorEnums(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
