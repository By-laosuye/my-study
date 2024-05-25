package com.laosuye.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.laosuye.demo.entity.appRelationDTO;
import com.laosuye.demo.service.impl.AppRelationExcelListener;
import com.laosuye.demo.service.IUserAppRelationService;
import com.laosuye.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserAppRelationService userAppRelationService;


    @GetMapping("/test")
    public String test(){
        String fileName = "C:\\Users\\老苏叶\\Desktop\\User.xlsx";

        String fileName2 = "C:\\Users\\老苏叶\\Desktop\\uc_user_app_relation数据.xlsx";
        EasyExcel.read(fileName2, appRelationDTO.class, new AppRelationExcelListener(userAppRelationService)).sheet().doRead();
        return "成功";
    }


}
