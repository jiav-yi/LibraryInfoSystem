package com.jnu.sys.controller;

import com.jnu.commmon.vo.Result;
import com.jnu.sys.entity.Books;
import com.jnu.sys.service.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private IBooksService booksService;

    @GetMapping("/all")
    public Result<List<Books>> getAllBooks() {
        List<Books> list = booksService.list();
        return Result.success(list, "查询成功");
    }
}
