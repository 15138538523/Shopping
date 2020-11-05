package com.qfedu.dto;

import lombok.Data;

import java.util.List;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  20:19
 * description:
 */
@Data
public class OrderDto {
    private List<OrderGoodsDto> list;
    private int type;//标记位 1.立即购买 2.购物车
    private int uid; //用户id
    private int aid;//收货地址
    private Integer paytype;//支付方式
    private String skuids;//skuid-skuid
}
