package com.laosuye.excel.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.laosuye.excel.entity.UserDTO;

import com.laosuye.excel.service.StudentService;
import com.laosuye.excel.service.IUserAppRelationService;
import com.laosuye.excel.service.IUserService;
import com.laosuye.excel.service.impl.UserExcelListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserAppRelationService userAppRelationService;

    @Autowired
    private StudentService studentService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    @GetMapping("/test")
    public void importExcel(){
        String fileName = "C:\\Users\\laosuye\\Desktop\\user.xlsx";

        List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int num = i;
            tasks.add(() -> {
                try {
                    EasyExcel.read(fileName, UserDTO.class, new UserExcelListener(studentService))
                            .excelType(ExcelTypeEnum.XLSX).sheet(num).doRead();
                } catch (Exception e) {
                    // 记录日志或者处理异常
                    log.error("解析异常",e);
                }
                return null;
            });

        }
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
