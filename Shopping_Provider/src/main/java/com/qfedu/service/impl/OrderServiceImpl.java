package com.qfedu.service.impl;

import com.qfedu.config.OrderType;
import com.qfedu.config.RabbitMQConfig;
import com.qfedu.config.RedisKeyConfig;
import com.qfedu.config.RedissonUtil;
import com.qfedu.dao.OrderDao;
import com.qfedu.dao.OrderItemDao;
import com.qfedu.dto.OrderDto;
import com.qfedu.dto.OrderGoodsDto;
import com.qfedu.entity.OrderForm;
import com.qfedu.entity.OrderItem;
import com.qfedu.service.OrderService;
import com.qfedu.util.IdGeneratorSinglon;
import com.qfedu.vo.R;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  21:06
 * description:
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao dao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private RabbitTemplate template;
    //下单
    @Override
    public R order(OrderDto dto) {
        //下单接口
        //校验
        if(dto != null){
            if(dto.getAid()>0 && dto.getUid()>0 && dto.getList() != null){
                //计算
                List<Object> list =new ArrayList<>();
                OrderForm orderForm = new OrderForm();
                orderForm.setId(IdGeneratorSinglon.getInstance().generator.nextId());
                int t = 0;
                Random random = new Random();
                try {
                //给商品加锁
                RedissonUtil.lock(RedisKeyConfig.ORDER_LOCK+dto.getSkuids());
                //遍历商品集合里的商品信息
                for (OrderGoodsDto od : dto.getList()) {
                    OrderItem orderItem = new OrderItem();
                    //从list里面获取商品的信息
                    orderItem.setOid(orderForm.getId());
                    orderItem.setSkuid(od.getSkuid());
                    //查询数据库  获取商品的最新价格和库存
                    orderItem.setPrice(random.nextInt(100)+1);
                    if(od.getCount()>random.nextInt(od.getCount()+1)){
                        return R.fail("亲，库存不足");
                    }else {
                        orderItem.setCount(od.getCount());
                    }
                    t+=orderItem.getCount()*orderItem.getPrice();
                    list.add(orderItem);
                }
                orderForm.setTotalmoney(t);
                orderForm.setTotalmoney(dto.getPaytype());
                int fm = 0;
                    //调用优惠卷服务
                    //调用满减服务
                    //调用积分抵扣 (京东-京豆  淘宝-淘金币)
                    //调用红包抵扣
                    //调用会员价格
                fm =  new Random().nextInt(orderForm.getTotalmoney()-100);
                //优惠劵
                orderForm.setFreemoney(fm);
                //支付的钱减去优惠劵
                orderForm.setPaymoney(t - fm);
                //生成订单
                orderForm.setFlag(OrderType.待支付.getVal());
                //把订单添加数据库
                dao.add(orderForm);
                //把订单详情添加到订单详情数据库
                orderItemDao.insertBatch(list);
                     //更改库存
                    //调用库存服务实现库存数量的更改
                    //生成系统消息
                }finally {
                //开锁
            RedissonUtil.unlock(RedisKeyConfig.ORDER_LOCK+dto.getSkuids());
                }
                return R.ok();
            }
        }
        return R.fail();
    }

    @Override
    public R orderV2(OrderDto dto) {
        if(dto != null){
            if(dto.getUid()>0 && dto.getAid()>0 && dto.getList() != null){
                //计算
                Map<String,Object> map = new LinkedHashMap<>();
                OrderForm orderForm = new OrderForm();
                orderForm.setId(IdGeneratorSinglon.getInstance().generator.nextId());
                int t = 0;
                Random random = new Random();
                try {
                    //给商品加锁
                    RedissonUtil.lock(RedisKeyConfig.ORDER_LOCK+dto.getSkuids());
                    //遍历商品集合的信息
                    for (OrderGoodsDto od : dto.getList()) {
                        OrderItem orderItem = new OrderItem();
                        //从商品详情的集合里获取商品的信息
                       orderItem.setOid(orderForm.getId());
                       orderItem.setSkuid(od.getSkuid());
                       //查询数据库  获取商品的最新的价格好商品的信息
                        //随机生成商品的价格
                        orderItem.setPrice(random.nextInt(100)+1);
                        //随机生成商品数量
                        if(od.getCount()>random.nextInt(od.getCount()+1)){
                                return R.fail("库存不足");
                        }else {
                            orderItem.setCount(od.getCount());
                        }
                        //计算价格
                        t+= orderItem.getCount() * orderItem.getSkuid();
                        map.put(od.getSkuid()+"",orderItem);
                    }
                    //设置价格
                    orderForm.setTotalmoney(t);
                    //选择支付方式
                    orderForm.setPaytype(orderForm.getPaytype());
                    int fm = 0;
                    //调用优惠卷服务
                    //调用满减服务
                    //调用积分抵扣 (京东-京豆  淘宝-淘金币)
                    //调用红包抵扣
                    //调用会员价格
                    fm = new Random().nextInt(orderForm.getTotalmoney()-100);
                    //设置优惠劵、
                    orderForm.setPaymoney(fm);
                    //总价钱减去优惠劵
                    orderForm.setTotalmoney(t - fm);
                    //生成订单
                    orderForm.setFlag(OrderType.待支付.getVal());
                    RedissonUtil.setStr(RedisKeyConfig.ORDER_V2+orderForm.getId(),orderForm);
                    RedissonUtil.setHashAll(RedisKeyConfig.ORDER_V2+orderForm.getId(),map);
                    //设置订单过期时间
                    RedissonUtil.setTime(RedisKeyConfig.ORDER_V2+orderForm.getId(),RedisKeyConfig.ORDER_TIME, TimeUnit.HOURS);
                    //更改库存
                    //调用库存服务实现库存数量的更改
                    //生成系统消息
                    //发送MQ消息  实现1.Mysql数据同步 2.实现延迟队列 -超时订单
                    template.convertAndSend(RabbitMQConfig.exorder,"",orderForm.getId());
                }finally {
                    //解除锁
                    RedissonUtil.unlock(RedisKeyConfig.ORDER_LOCK+dto.getSkuids());
                }
            }
        }
        return R.fail();
    }
    //查询全部
    @Override
    public R queryUid(int uid) {
        return R.ok(dao.queryByUid(uid));
    }
}
