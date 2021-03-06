package com.qianxu.mall.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductReq {
    @NotNull
    private Integer id;

    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    @Min(value = 1, message = "商品价格不能小于1分")
    private Integer price;

    @Max(value = 10000, message = "商品库存不能大于10000")
    private Integer stock;

    private Integer status;
}