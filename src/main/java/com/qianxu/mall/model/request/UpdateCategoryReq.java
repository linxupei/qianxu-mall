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
public class UpdateCategoryReq {

    @NotNull
    private Integer id;

    @Size(min = 2, max = 5)
    private String name;

    @Max(3)
    private Integer type;

    private Integer parentId;

    private Integer orderNum;

}
