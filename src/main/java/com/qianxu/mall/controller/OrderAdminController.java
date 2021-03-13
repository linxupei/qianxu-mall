package com.qianxu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.common.ApiRestResponse;
import com.qianxu.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 16:37
 * @describe
 */
@RestController
public class OrderAdminController {

    @Autowired
    OrderService orderService;

    @ApiOperation("管理员订单列表")
    @GetMapping({"/admin/order/list"})
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("支付订单")
    @PutMapping({"pay"})
    public ApiRestResponse pay(@RequestParam String orderNo) {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("管理员发货")
    @PutMapping({"/admin/order/delivered"})
    public ApiRestResponse delivered(@RequestParam String orderNo) {
        orderService.delivered(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("完结订单")
    @PutMapping({"/order/finish"})
    public ApiRestResponse finish(@RequestParam String orderNo) {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}
