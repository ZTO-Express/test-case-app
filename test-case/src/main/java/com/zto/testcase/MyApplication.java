package com.zto.testcase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@EnableScheduling
@EnableCaching
public class MyApplication {

    public static void main(String[] args) {
        log.info(">>>>>>启动开始");
        SpringApplication.run(MyApplication.class, args);
        log.info(">>>>>>启动结束");
    }
}
