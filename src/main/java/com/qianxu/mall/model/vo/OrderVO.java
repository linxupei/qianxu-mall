package com.qianxu.mall.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 14:45
 * @describe
 */
@Data
public class OrderVO {
    private String orderNo;

    private Integer userId;

    private Integer totalPrice;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private Integer orderStatus;

    private Integer postage;

    private Integer paymentType;

    private Date deliveryTime;

    private Date payTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private String OrderStatusName;

    private List<OrderItemVO> orderItemVOList;
}
