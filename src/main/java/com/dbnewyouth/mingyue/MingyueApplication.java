package com.dbnewyouth.mingyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启事务
@ServletComponentScan //执行过滤器
@MapperScan("com.dbnewyouth.mingyue.**.mapper")
public class MingyueApplication {

    public static void main(String[] args) {
        SpringApplication.run(MingyueApplication.class, args);
    }

}
