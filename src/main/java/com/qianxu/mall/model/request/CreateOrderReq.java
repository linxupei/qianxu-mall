package com.qianxu.mall.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateOrderReq {

    @NotNull
    private String receiverName;

    @NotNull
    private String receiverMobile;

    @NotNull
    private String receiverAddress;

    private Integer orderStatus;

    private Integer postage = 0;

    private Integer paymentType = 1;
}