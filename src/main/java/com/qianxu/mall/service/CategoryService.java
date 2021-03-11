package com.qianxu.mall.service;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.model.pojo.Category;
import com.qianxu.mall.model.request.AddCategoryReq;
import com.qianxu.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/7 15:18
 * @describe
 */
public interface CategoryService {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer();
}
