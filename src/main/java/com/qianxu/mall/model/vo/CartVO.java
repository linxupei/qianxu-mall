package com.qianxu.mall.model.vo;

import lombok.Data;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/12 15:27
 * @describe
 */
@Data
public class CartVO {
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;

    private Integer price;

    private Integer totalPrice;

    private String productName;

    private String productImage;
}
