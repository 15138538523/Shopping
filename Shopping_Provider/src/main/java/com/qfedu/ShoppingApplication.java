package com.qfedu;

import lombok.NoArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * projectName: Shopping
 * author: 崔
 * time: 2020/11/05  11:37
 * description:启动类
 */
@SpringBootApplication  //启动springBoot项目
@MapperScan(basePackages = "com.qfedu.dao")
@EnableDiscoveryClient  //发现注册与配置中心
@EnableScheduling //开启定时任务
public class ShoppingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class,args);
    }
}
