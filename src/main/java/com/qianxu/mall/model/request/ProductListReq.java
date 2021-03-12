package com.qianxu.mall.model.request;

import lombok.Data;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/11 22:17
 * @describe 商品详情
 */
@Data
public class ProductListReq {

    private String keyword;

    private Integer categoryId;

    private String orderBy;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
