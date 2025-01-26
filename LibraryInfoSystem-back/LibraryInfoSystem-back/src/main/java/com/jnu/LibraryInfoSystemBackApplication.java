package com.jnu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jnu.*.mapper")
public class LibraryInfoSystemBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryInfoSystemBackApplication.class, args);
    }

}
