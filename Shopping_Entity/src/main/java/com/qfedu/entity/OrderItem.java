package com.qfedu.entity;

import lombok.Data;

import java.util.Date;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  21:46
 * description:商品详情表
 */
@Data
public class OrderItem {
    private Integer id;
    private Long oid;
    private Integer skuid;
    private Integer count;
    private Integer price;
    private Date ctime;
}
