package com.qianxu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.model.pojo.Product;
import com.qianxu.mall.model.request.ProductListReq;
import com.qianxu.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/11 22:11
 * @describe 前台商品controller
 */
@RestController
public class ProductUserController {
    @Autowired
    private ProductService productService;

    @ApiOperation("商品详情列表")
    @GetMapping("product/detail")
    public ApiRestResponse detail(Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("商品列表")
    @GetMapping("product/list")
    public ApiRestResponse list(ProductListReq productListReq) {
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
