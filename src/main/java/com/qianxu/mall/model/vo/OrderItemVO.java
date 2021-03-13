package com.qianxu.mall.model.vo;

import lombok.Data;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 14:47
 * @describe
 */
@Data
public class OrderItemVO {
    private String orderNo;

    private Integer productId;

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;
}
