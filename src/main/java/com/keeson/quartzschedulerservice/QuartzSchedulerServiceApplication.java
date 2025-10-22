package com.keeson.quartzschedulerservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@EnableDiscoveryClient
@ComponentScan(
        basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*", "com.keeson"})
@EntityScan(basePackages = {"com.keeson"})
@MapperScan(basePackages = {"com.gitee.sunchenbin.mybatis.actable.dao.*", "com.keeson.quartzschedulerservice.infrastructure.data.mybatis"})
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class QuartzSchedulerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzSchedulerServiceApplication.class, args);
    }

}
