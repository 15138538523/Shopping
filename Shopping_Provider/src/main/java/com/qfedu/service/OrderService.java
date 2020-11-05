package com.qfedu.service;

import com.qfedu.dto.OrderDto;
import com.qfedu.vo.R;

public interface OrderService {
    //下单接口_订单预览页面
    R order(OrderDto dto);
    //1,订单超时 2，redis 3，同步到mysql
    R orderV2(OrderDto dto);
    //我的订单
    R queryUid(int uid);
}
