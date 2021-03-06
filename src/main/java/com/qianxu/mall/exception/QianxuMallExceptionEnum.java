package com.qianxu.mall.exception;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/6 16:15
 * @describe 异常枚举
 */
public enum QianxuMallExceptionEnum {
    NEED_USER_NAME(10001, "用户名不能为空"),
    NEED_PASSWORD(10002, "密码不能为空"),
    PASSWORD_TOO_SHORT(10003, "密码长度不能小于8位"),
    NAME_EXIST(10004, "不允许重名"),
    INSERT_FAILED(10005, "插入失败, 请重试"),
    WRONG_PASSWORD(10006, "密码错误"),
    NEED_LOGIN(10007, "用户未登录"),
    UPDATE_FAILED(10008, "更新失败"),
    NEED_ADMIN(10009, "无管理员权限"),
    PARAM_NOT_NULL(100010, "参数不能为空"),
    CREATE_FAILED(100011, "新增失败"),
    REQUEST_PARAM_ERROR(100012, "参数错误"),
    DELETE_FAILED(100013, "删除失败"),
    MKDIR_FAILED(100014, "文件夹创建失败"),
    UPLOAD_FAILED(100015, "图片上传失败失败"),
    NOT_SALE(100016, "商品状态不可售"),
    NOT_ENOUGH(100017, "商品库存不足"),
    CART_EMPTY(100018, "购物车已勾选的商品为空"),
    NOT_ENUM(100019, "未找到对应枚举类"),
    NO_ORDER(100020, "订单不存在"),
    NOT_YOUR_ORDER(100021, "订单不属于你"),
    WRONG_ORDER_STATUS(100022, "订单状态不符"),
    SYSTEM_ERROR(20000, "系统异常");

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String msg;

    QianxuMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
