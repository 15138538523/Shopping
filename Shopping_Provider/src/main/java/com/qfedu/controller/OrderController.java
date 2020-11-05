package com.qfedu.controller;

import com.qfedu.dto.OrderDto;
import com.qfedu.service.OrderService;
import com.qfedu.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/06  0:14
 * description:
 */
@RestController
@RequestMapping("/provider/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //下单
    @PostMapping("/createorder.do")
    public R order(@RequestBody OrderDto dto){
        return orderService.orderV2(dto);
    }
    //查看全部订单
    public R query(@RequestBody int uid){
        return orderService.queryUid(uid);
    }

}
