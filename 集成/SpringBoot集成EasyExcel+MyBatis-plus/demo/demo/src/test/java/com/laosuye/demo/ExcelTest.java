package com.laosuye.demo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Types;
import java.util.Collections;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2024/4/11 18:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ExcelTest {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://39.106.35.36:3306/myStudy?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC",
                        "root", "YEye1123")
                .globalConfig(builder -> {
                    builder.author("laosuye") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("D:\\study\\java\\code\\my-study\\集成\\SpringBoot集成EasyExcel+MyBatis-plus\\demo\\demo\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.laosuye.demo") // 设置父包名
                                .moduleName("demo") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\study\\java\\code\\my-study\\集成\\SpringBoot集成EasyExcel+MyBatis-plus\\demo\\demo\\src\\main\\resources")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("uc_user_app_relation") // 设置需要生成的表名
                                .addTablePrefix("uc_", "c_") // 设置过滤表前缀
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
