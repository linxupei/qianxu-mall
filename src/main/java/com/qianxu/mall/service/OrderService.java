package com.qianxu.mall.service;

import com.github.pagehelper.PageInfo;
import com.qianxu.mall.model.request.CreateOrderReq;
import com.qianxu.mall.model.vo.OrderVO;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 10:40
 * @describe 订单Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void pay(String orderNo);

    void delivered(String orderNo);

    void finish(String orderNo);
}
