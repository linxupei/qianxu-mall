package com.qianxu.mall.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/7 14:59
 * @describe AddCategoryReq
 */
@Data
public class AddCategoryReq {

    @Size(min = 2, max = 5)
    @NotNull(message = "name不能为null")
    private String name;

    @Max(3)
    @NotNull(message = "type不能为null")
    private Integer type;

    @NotNull(message = "parentId不能为null")
    private Integer parentId;

    @NotNull(message = "orderNum不能为null")
    private Integer orderNum;

}
