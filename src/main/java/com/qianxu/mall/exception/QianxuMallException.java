package com.qianxu.mall.exception;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/6 16:32
 * @describe
 */
public class QianxuMallException extends RuntimeException{
    private final Integer code;
    private final String msg;

    public QianxuMallException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public QianxuMallException(QianxuMallExceptionEnum ex) {
        this(ex.getCode(), ex.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
