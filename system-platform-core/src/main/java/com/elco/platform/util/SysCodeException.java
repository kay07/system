package com.elco.platform.util;

/**
 * @author kay
 * @date 2021/8/13
 */
public class SysCodeException extends RuntimeException{
    private String code;
    private String message;
    public SysCodeException(){super();}
    public SysCodeException(SysCodeEnum sysCodeEnum){
        super(sysCodeEnum.getCode()+"->"+sysCodeEnum.getMessage());
        this.code=sysCodeEnum.getCode();
        this.message=sysCodeEnum.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
