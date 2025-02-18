package com.jnu;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql:///book_db";
        String username = "root";
        String password = "zjwzjw123";
        String author = "jiav";
        String outputDir = "E:\\Programme\\graduation\\LibraryInfoSystem-back\\LibraryInfoSystem-back\\src\\main\\java";
        String basePackage = "com.jnu";
        String moduleName = "sys";
        String mapperLocation = "E:\\Programme\\graduation\\LibraryInfoSystem-back\\LibraryInfoSystem-back\\src\\main\\resources\\mapper\\" + moduleName;
        String tableName = "book";
//        String tablePrefix = "x_";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(basePackage) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName);
                            //.entityBuilder().fileOverride().mapperBuilder().fileOverride(); // 设置需要生成的表名
//                            .addTablePrefix(tablePrefix); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
