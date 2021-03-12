package com.qianxu.mall.model.query;

import lombok.Data;

import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/11 22:21
 * @describe
 */
@Data
public class ProductListQuery {
    private String keyword;

    private List<Integer> categoryIds;
}
