package com.qfedu.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/06  0:07
 * description:
 */

public class RabbitMQConfig {
    @Value("${order.timeout}")
    private long otimeout;
    //定义队列
    //实现下单数据的同步 Redis->Mysql
    public String ordersync="cloud-order-sync";
    //实现延迟队列-订单超时  24小时
    public String ordertime="cloud-order-time";

    public String ordertimeout="cloud-order-timeout";
    @Bean
    public Queue createQ1(){
        return new Queue(ordersync);
    }
    @Bean
    public Queue createQ2(){
        Map<String,Object> params=new LinkedHashMap<>();
        //
        params.put("x-messgae-ttl",otimeout);
        //
        params.put("x-dead-letter-exchange",exorderttl);
        //
        params.put("x-dead-letter-routing-key","order-timeout");
        return QueueBuilder.durable(ordertime).withArguments(params).build();
    }
    @Bean
    public Queue createQ3(){
        return new Queue(ordertimeout);
    }
    //交换器
    public static String exorder="ex-order";//fanout
    public static String exorderttl="ex-order-ttl";//死信交换器
    @Bean
    public FanoutExchange createFex(){
        return new FanoutExchange(exorder);
    }
    @Bean
    public DirectExchange createDex(){
        return new DirectExchange(exorderttl);
    }
    //绑定
    @Bean
    public Binding createBdMysql(FanoutExchange fe){
        return BindingBuilder.bind(createQ1()).to(fe);
    }
    @Bean
    public Binding createBdTtl(FanoutExchange fe){
        return BindingBuilder.bind(createQ2()).to(fe);
    }
    @Bean
    public Binding createBdDlx(DirectExchange de){
        return BindingBuilder.bind(createQ3()).to(de).with("order-timeout");
    }
}
