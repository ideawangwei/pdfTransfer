package com.bonc.pdf.exception;

/**
 * Created by ldf on 2018/2/28.
 */
public class ServiceException extends RuntimeException {

    private SysErrorEnums exceptionEnums;
    public ServiceException(SysErrorEnums exceptionEnums){
        this.exceptionEnums = exceptionEnums;
    }
    public SysErrorEnums getExceptionEnums(){
        return exceptionEnums;
    }
}
