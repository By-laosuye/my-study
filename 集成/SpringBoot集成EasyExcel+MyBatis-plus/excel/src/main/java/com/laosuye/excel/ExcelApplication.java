package com.laosuye.excel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 启动类
 * @version：1.0
 * @Author：yelixian
 * @description：
 * @data：2024-05-27-上午8:53
 */
@MapperScan("com.laosuye.excel.dao")
@SpringBootApplication
public class ExcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExcelApplication.class, args);
    }
}
