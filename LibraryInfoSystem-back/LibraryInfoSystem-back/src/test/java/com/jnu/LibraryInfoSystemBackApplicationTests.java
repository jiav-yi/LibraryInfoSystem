package com.jnu;

import com.jnu.sys.entity.Books;
import com.jnu.sys.mapper.BooksMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class LibraryInfoSystemBackApplicationTests {

    @Resource
    private BooksMapper booksMapper;

    @Test
    void testMapper() {
        List<Books> books = booksMapper.selectList(null);
        books.forEach(System.out::println);
    }
}
